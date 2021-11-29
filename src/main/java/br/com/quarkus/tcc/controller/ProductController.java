package br.com.quarkus.tcc.controller;

import br.com.quarkus.tcc.model.Product;
import br.com.quarkus.tcc.repository.ProductRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.status;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductRepository productRepository;

    @GET
    public List<Product> list() {return productRepository.listAll();}

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Optional<Product> optProduct = productRepository.findByIdOptional(id);
        return optProduct.map(product -> status(OK).entity(product).build()).orElseGet(() -> status(NOT_FOUND).build());
    }

    @POST
    @Transactional
    public Response saveProduct(Product product) {
        productRepository.persist(product);
        if (!productRepository.isPersistent(product)) {return status(NOT_FOUND).build();}
        return status(CREATED).entity(product).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Product productToSave){
        Product product = productRepository.findById(id);
        if (Objects.isNull(product)){return status(NOT_FOUND).build();}
        mergeProductUpdate(productToSave, product);
        productRepository.persist(product);
        return status(OK).entity(product).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Product product = productRepository.findById(id);
        if (Objects.isNull(product)){return status(NOT_FOUND).build();}
        productRepository.delete(product);
        return status(OK).entity(product).build();
    }

    @GET
    @Path("/bycode/{code}")
    public Response search(@PathParam("code") String code) {
        Product product = productRepository.findByCode(code);
        if (Objects.isNull(product)){return status(NOT_FOUND).build();}
        return status(OK).entity(product).build();
    }

    private void mergeProductUpdate(Product productToSave, Product product) {
        product.setCode(productToSave.getCode());
        product.setColor(productToSave.getColor());
        product.setPrice(productToSave.getPrice());
        product.setModel(productToSave.getModel());
        product.setName(productToSave.getName());
    }
}
