package br.com.quarkus.tcc.repository;

import br.com.quarkus.tcc.model.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public Product findByCode(String code){
        return find("code", code).firstResult();
    }
}
