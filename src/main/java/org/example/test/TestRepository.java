package org.example.test;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TestRepository implements PanacheRepository<DatabaseInfo> {

    public Uni<List<DatabaseInfo>> getDatabaseInfo() {
        return findAll().list();
    }
}
