package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The PropertyRepository interface provides methods to perform CRUD operations on Property entities.
 * It extends JpaRepository, which provides standard methods for database access.
 */
public interface PropertyRepository extends JpaRepository<Property, Long> {

    /**
     * Custom query method to find properties by their address.
     * 
     * @param address the address to search for
     * @return a list of properties that match the given address
     */
    List<Property> findByAddress(String address);
}
