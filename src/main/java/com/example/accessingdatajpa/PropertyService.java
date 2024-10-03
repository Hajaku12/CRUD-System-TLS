package com.example.accessingdatajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PropertyService {


    @Autowired
    private PropertyRepository propertyRepository;

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Optional<Property> getPropertyById(Long id) {
        return propertyRepository.findById(id);
    }

    public Optional<Property> updateProperty(Long id, Property propertyDetails) {
        return propertyRepository.findById(id)
                .map(property -> {
                    property.setAddress(propertyDetails.getAddress());
                    property.setPrice(propertyDetails.getPrice());
                    property.setSize(propertyDetails.getSize());
                    property.setDescription(propertyDetails.getDescription());
                    return propertyRepository.save(property);
                });
    }

    public boolean deleteProperty(Long id) {
        return propertyRepository.findById(id)
                .map(property -> {
                    propertyRepository.delete(property);
                    return true;
                }).orElse(false);
    }
}
