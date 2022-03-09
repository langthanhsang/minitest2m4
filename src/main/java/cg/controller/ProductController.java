package cg.controller;


import cg.model.Category;
import cg.model.Product;
import cg.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@CrossOrigin("*")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @GetMapping
    public ResponseEntity<Iterable<Product>> showAll() {
        Iterable<Product> products = iProductService.findAll();
        if (!products.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Product>> showAllPage(@PageableDefault(value = 10) Pageable pageable) {
        Page<Product> products = iProductService.findPage(pageable);
        if (!products.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/cate")
    public ResponseEntity<Iterable<Category>> showAllCate() {
        Iterable<Category> categories = iProductService.fillAddCate();
        if (!categories.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> showOne(@PathVariable("id") Long id) {
        Optional<Product> product = iProductService.findById(id);
        if (!product.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product){
        Product productCreate = iProductService.save(product);
        return new ResponseEntity<>(productCreate, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> editProduct(@RequestBody Product productEdit, @PathVariable("id") Long id){
        Optional<Product> product = iProductService.findById(id);
        if(!product.isPresent()){
            new  ResponseEntity<>( productEdit,HttpStatus.NOT_FOUND);
        }
        productEdit.setId(product.get().getId());
        productEdit = iProductService.save(productEdit);
        return  new ResponseEntity<>(productEdit,HttpStatus.OK);

    }
    @DeleteMapping("{id}")
    public ResponseEntity<Product> delete(@PathVariable("id") Long id) {
        Optional<Product> product = iProductService.findById(id);
        if (!product.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        iProductService.delete(id);
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Iterable<Product>> showAllByName(@RequestParam("search") String search) {
        Iterable<Product> products = iProductService.findAllByName(search);
        if (!products.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


}
