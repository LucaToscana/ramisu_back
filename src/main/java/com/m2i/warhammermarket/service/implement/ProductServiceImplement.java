package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.repository.ProductRepository;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.service.ProductService;
import com.m2i.warhammermarket.service.UserService;
import com.m2i.warhammermarket.service.mapper.ProductMapper;
import com.m2i.warhammermarket.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImplement implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductRepository productRepository;

    public ProductServiceImplement() {
    }

    @Override
    public ProductDTO save(ProductDTO product) {
        return null;
    }

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        return this.productRepository.findAll(pageable).map(this.productMapper::productToProductDTO);
    }

    @Override
    public Optional<ProductDTO> findOne(Long id) {
        return Optional.ofNullable(this.productMapper.productToProductDTO(this.productRepository.findById(id).orElse(null)));
    }

    @Override
    public void delete(Long id) {

    }
}
