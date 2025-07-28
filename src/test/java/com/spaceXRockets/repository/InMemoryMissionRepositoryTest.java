package com.spaceXRockets.repository;

import com.spaceXRockets.model.Mission;
import com.spaceXRockets.repository.inmemory.InMemoryMissionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryMissionRepositoryTest {

    @Test
    void addAndRetrieveMissions() {
        InMemoryMissionRepository repository = new InMemoryMissionRepository();
        Mission m1 = Mission.builder().name("Luna1").build();
        Mission m2 = Mission.builder().name("Mars").build();

        repository.add(m1);
        repository.add(m2);

        List<Mission> all = repository.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(m1));
        assertTrue(all.contains(m2));
    }

    @Test
    void repositoryIsolatedStorage() {
        InMemoryMissionRepository repo1 = new InMemoryMissionRepository();
        InMemoryMissionRepository repo2 = new InMemoryMissionRepository();

        repo1.add(Mission.builder().name("A").build());

        assertTrue(repo2.getAll().isEmpty());
    }
}