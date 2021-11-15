package com.m2i.warhammermarket.service.mapper;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductMapper {

    public List<ProductDTO> usersToUsersDTO(List<ProductDAO> products) {
        return products.stream()
                .filter(Objects::nonNull)
                .map(this::productToProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO productToProductDTO(ProductDAO product) {
        return new ProductDTO(product);
    }

    public List<ProductDAO> productDTOsToProductss(List<ProductDTO> productDTOs) {
        return productDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::productDTOToProduct)
                .collect(Collectors.toList());
    }

    public ProductDAO productDTOToProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        } else {
            ProductDAO product = new ProductDAO();
            product.setId(productDTO.getId());
            product.setEan13(productDTO.getEan13());
            product.setLabel(productDTO.getLabel());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setPromotion(productDTO.getPromotion());
            product.setStock(productDTO.getStock());
            product.setYearOfProduction(productDTO.getYearOfProduction());
            return product;
        }
    }

    public ProductDAO productFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductDAO product = new ProductDAO();
        product.setId(id);
        return product;
    }

}
