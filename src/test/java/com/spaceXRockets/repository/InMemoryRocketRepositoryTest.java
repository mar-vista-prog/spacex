package com.spaceXRockets.repository;

import com.spaceXRockets.model.Rocket;
import com.spaceXRockets.repository.inmemory.InMemoryRocketRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRocketRepositoryTest {

    @Test
    void addAndRetrieveRockets() {
        InMemoryRocketRepository repository = new InMemoryRocketRepository();
        Rocket r1 = Rocket.builder().name("Dragon 1").build();
        Rocket r2 = Rocket.builder().name("Dragon 2").build();

        repository.add(r1);
        repository.add(r2);

        List<Rocket> all = repository.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(r1));
        assertTrue(all.contains(r2));
    }
}