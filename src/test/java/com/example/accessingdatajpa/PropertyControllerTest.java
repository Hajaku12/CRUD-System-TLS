package com.example.accessingdatajpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class PropertyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

    private Property property;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(propertyController).build();

        property = new Property("Calle 1 No. 2-3", "Casa", 256.0, 10.5);
        property.setId(1L);
    }

    @Test
    public void testCreateProperty() throws Exception {
        when(propertyService.createProperty(any(Property.class))).thenReturn(property);

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"Calle 1 No. 2-3\",\"description\":\"Casa\",\"price\":256.0,\"size\":10.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Calle 1 No. 2-3"))
                .andExpect(jsonPath("$.price").value(256.0));

        verify(propertyService, times(1)).createProperty(any(Property.class));
    }

    @Test
    public void testGetAllProperties() throws Exception {
        List<Property> properties = Arrays.asList(property);
        when(propertyService.getAllProperties()).thenReturn(properties);

        mockMvc.perform(get("/api/properties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("Calle 1 No. 2-3"));

        verify(propertyService, times(1)).getAllProperties();
    }

    @Test
    public void testGetPropertyById() throws Exception {
        when(propertyService.getPropertyById(1L)).thenReturn(Optional.of(property));

        mockMvc.perform(get("/api/properties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Calle 1 No. 2-3"));

        verify(propertyService, times(1)).getPropertyById(1L);
    }

    @Test
    public void testUpdateProperty() throws Exception {
        Property updatedProperty = new Property("Calle 2 No. 3-4", "Actualizado", 567.0, 15.0);
        when(propertyService.updateProperty(anyLong(), any(Property.class))).thenReturn(Optional.of(updatedProperty));

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"Calle 2 No. 3-4\",\"description\":\"Actualizado\",\"price\":567.0,\"size\":15.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Calle 2 No. 3-4"))
                .andExpect(jsonPath("$.price").value(567.0));

        verify(propertyService, times(1)).updateProperty(anyLong(), any(Property.class));
    }

    @Test
    public void testDeleteProperty() throws Exception {
        when(propertyService.deleteProperty(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/properties/1"))
                .andExpect(status().isOk());

        verify(propertyService, times(1)).deleteProperty(1L);
    }
}
