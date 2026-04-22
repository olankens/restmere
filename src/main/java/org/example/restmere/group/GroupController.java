package org.example.restmere.group;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final AtomicLong counter = new AtomicLong(1);
    private final List<Group> groups = new ArrayList<>();

    @GetMapping("")
    List<Group> findAll() {
        return groups;
    }

    @GetMapping("/{id}")
    Group findById(@PathVariable Long id) {
        return groups.stream()
                .filter(group -> group.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Group with id " + id + " not found"
                ));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    Group create(@RequestBody Group group) {
        Group created = new Group(
                counter.getAndIncrement(),
                group.name(),
                group.description(),
                group.city(),
                group.organizer(),
                group.createdAt() != null ? group.createdAt() : LocalDate.now()
        );
        groups.add(created);
        return created;
    }

    @PutMapping("/{id}")
    Group update(@PathVariable Long id, @RequestBody Group group) {
        Group current = findById(id);
        Group updated = new Group(
                id,
                group.name(),
                group.description(),
                group.city(),
                group.organizer(),
                group.createdAt()
        );
        groups.remove(current);
        groups.add(updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id) {
        Group current = findById(id);
        groups.remove(current);
    }

    @PostConstruct
    private void bootstrap() {
        groups.add(new Group(
                counter.getAndIncrement(),
                "Brussels Spring Meetup",
                "Spring developers in Brussels",
                "Brussels",
                "John Doe",
                LocalDate.of(1970, 1, 1)
        ));
    }

}
