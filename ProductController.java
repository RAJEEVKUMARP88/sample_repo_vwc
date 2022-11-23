package com.sb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.exception.ResourceNotFoundException;
import com.sb.model.Product1;
import com.sb.repository.ProductRepository;
import com.sb.service.ProductService;

@RestController
@RequestMapping("/proApi")
public class ProductController {
	@Autowired
	ProductService ps;
	
	@Autowired
	ProductRepository productRepository;
	
	@PostMapping("/create")
	public ResponseEntity<Product1> createProduct(@Valid @RequestBody Product1 product) {
		try {
			Product1 prod1 = ps.saveOrUpdateProd(product);
			return new ResponseEntity<>(prod1, HttpStatus.CREATED);
		} 	catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/readAll")
	public ResponseEntity<List<Product1>> getAllProducts(@RequestParam(required=false) String name) {
		try {
			List<Product1> products = new ArrayList<Product1>();

			if (name == null)
				productRepository.findAll().forEach(products::add);
			else
				productRepository.findByNameContaining(name).forEach(products::add);

			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/product/{id}")
    public ResponseEntity < Product1 > getProductById
    (@PathVariable(value = "id") Integer productId)
    throws ResourceNotFoundException {
		Product1 prod = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Entered ProductId is not available in Database,Could you please check or try with other Emp Number :: " +productId));
		return ResponseEntity.ok().body(prod);
		}

}
