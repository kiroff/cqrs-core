# CQRS Core

This module contains the foundational infrastructure for implementing **CQRS (Command Query Responsibility Segregation)** and **Event Sourcing** in a Spring Boot environment.

## Overview

The `cqrs-core` module provides base classes, interfaces, and exceptions that are shared across the bank account microservices. It is designed to be reusable for other CQRS/ES-based projects.

## Components

- **Commands**: Base class for all commands (`BaseCommand`) and an interface for the `CommandDispatcher`.
- **Events**: Base class for all events (`BaseEvent`).
- **Domain**: Base entity and aggregate root (`AggregateRoot`) that tracks uncommitted changes.
- **Infrastructure**: Interfaces for `EventStore`, `EventProducer`, and `CommandDispatcher`.
- **Handlers**: Interface for `EventSourcingHandler`.
- **Producers**: Interface for publishing events (e.g., to Kafka).

## Usage

This module is a dependency for both the `bank-account-command` and `bank-account-query` modules. It defines the contract for how commands are dispatched and how events are stored and replayed.

---
Part of the [Bank Account Management System](../bank-account/README.md).