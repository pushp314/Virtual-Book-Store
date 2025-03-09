package com.virtualbookstore.service;

import com.virtualbookstore.model.Book;
import com.virtualbookstore.model.Cart;
import com.virtualbookstore.model.User;
import com.virtualbookstore.repository.BookRepository;
import com.virtualbookstore.repository.CartRepository;
import com.virtualbookstore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<Cart> getCartItems(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public Cart addToCart(Long userId, Long bookId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        if (book.getStock() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        // Check if the book is already in the cart
        Cart existingCartItem = cartRepository.findByUserIdAndBookId(userId, bookId);
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantity;
            if (newQuantity > book.getStock()) {
                throw new RuntimeException("Not enough stock available");
            }
            existingCartItem.setQuantity(newQuantity);
            book.setStock(book.getStock() - quantity); // Reduce stock
            bookRepository.save(book);
            return cartRepository.save(existingCartItem);
        }

        // If the book is not in the cart, create a new entry
        Cart cartItem = new Cart();
        cartItem.setUser(user);
        cartItem.setBook(book);
        cartItem.setQuantity(quantity);

        book.setStock(book.getStock() - quantity); // Reduce stock
        bookRepository.save(book);
        return cartRepository.save(cartItem);
    }

    @Transactional
    public void removeFromCart(Long id) {
        Cart cartItem = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Book book = cartItem.getBook();
        book.setStock(book.getStock() + cartItem.getQuantity()); // Restore stock
        bookRepository.save(book);

        cartRepository.deleteById(id);
    }

    @Transactional
    public Cart updateQuantity(Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        Book book = cart.getBook();
        int previousQuantity = cart.getQuantity();
        int stockChange = quantity - previousQuantity;

        if (book.getStock() < stockChange) {
            throw new RuntimeException("Not enough stock available");
        }

        cart.setQuantity(quantity);
        book.setStock(book.getStock() - stockChange); // Update stock
        bookRepository.save(book);

        return cartRepository.save(cart);
    }
}
