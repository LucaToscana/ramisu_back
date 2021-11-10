package com.m2i.warhammermarket.service.mapper;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductMapper {

    public List<ProductDTO> usersToUsersDTO(List<ProductDAO> productDAOS) {
        return productDAOS.stream()
                .filter(Objects::nonNull)
                .map(this::productToProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO productToProductDTO(ProductDAO productDAO) {
        return new ProductDTO(productDAO);
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
            ProductDAO productDAO = new ProductDAO();
            productDAO.setId(productDTO.getId());
            productDAO.setEan13(productDTO.getEan13());
            productDAO.setLabel(productDTO.getLabel());
            productDAO.setPrice(productDTO.getPrice());
            productDAO.setDescription(productDTO.getDescription());
            productDAO.setPromotion(productDTO.getPromotion());
            productDAO.setStock(productDTO.getStock());
            productDAO.setYearOfProduction(productDTO.getYearOfProduction());
            return productDAO;
        }
    }

    public ProductDAO productFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductDAO productDAO = new ProductDAO();
        productDAO.setId(id);
        return productDAO;
    }

}
