package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
