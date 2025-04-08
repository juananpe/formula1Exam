# Formula 1 Application Documentation

## Project Overview
The Formula 1 application is a Java-based desktop application built using JavaFX for the user interface and Hibernate/JPA for database persistence. The application allows users to manage Formula 1 drivers, teams, and races, providing functionality to track driver performance, team associations, and race participation.

## Architecture

The application follows a layered architecture with clear separation of concerns:

1. **Presentation Layer** - JavaFX UI components and controllers
2. **Business Logic Layer** - Service interfaces and implementations
3. **Data Access Layer** - Database interaction through Hibernate/JPA
4. **Domain Layer** - Entity classes representing the domain model

## Domain Model

### Core Entities

#### Pilot (Driver)
- Represents a Formula 1 driver with:
  - Personal information (name, nationality)
  - Performance metrics (points)
  - Team association (many-to-one relationship with Team)
  - Race participation (many-to-many relationship with Race)

#### Team
- Represents a Formula 1 team with:
  - Team information (name, country)
  - Collection of drivers (one-to-many relationship with Pilot)

#### Race
- Represents a Formula 1 race event with:
  - Event details (name, date)
  - Participating drivers (many-to-many relationship with Pilot)

#### RaceResult
- Represents the outcome of a driver's participation in a race:
  - Race reference (many-to-one relationship with Race)
  - Driver reference (many-to-one relationship with Pilot)
  - Position (finishing position in the race)
  - Points (points awarded for the position)

## Technical Implementation

### Technology Stack
- **Java:** Core programming language (Java 23)
- **JavaFX:** UI framework
- **FXML:** UI layout definition
- **Hibernate/JPA:** ORM for database persistence
- **H2 Database:** In-memory/file-based database
- **Maven:** Build and dependency management

### Package Structure
- `eus.ehu.domain` - Domain entities (Pilot, Team, Race, RaceResult)
- `eus.ehu.businesslogic` - Business logic interfaces and implementations
- `eus.ehu.data_access` - Database access and management
- `eus.ehu.presentation` - UI controllers and presentation logic

### Key Components

#### Database Access (DbAccessManager)
- Manages entity persistence and retrieval operations
- Handles entity relationships and transactions
- Provides query methods for domain entities

#### Business Logic
- Implements the `BlInterface` to provide service methods
- Acts as an intermediary between the presentation and data access layers
- Encapsulates business rules and logic

#### UI Controllers
- `MainLayoutController`: Manages the main application layout
- `DriversController`: Handles driver management operations
- `TeamsController`: Handles team management (currently minimal implementation)
- `RacesController`: Handles race management (currently minimal implementation)
- `ResultsController`: Handles race results and scoring management

#### UI Layout
- Uses FXML for defining UI components
- Main layout with navigation sidebar
- Table views for displaying entity lists
- Forms for data entry and modification

## Features

### Current Features
1. **Driver Management**
   - View list of drivers with their details (name, nationality, points)
   - Add new drivers
   - Delete existing drivers
   - Display driver count

2. **Team Management**
   - Associate drivers with teams
   - Store team information (name, country)

3. **Race Management**
   - Create race events
   - Associate drivers with races
   - Track race dates and locations

4. **Results Scoring**
   - Select a race to view participating drivers
   - Assign finishing positions to drivers
   - Award points based on positions
   - Update driver point totals automatically
   - Validate positions (no duplicates) and points
   - Edit existing race results

### Data Model Relationships
- Drivers belong to teams (many-to-one)
- Drivers participate in races (many-to-many)
- Teams have multiple drivers (one-to-many)
- Race results connect drivers to races with position and points data (many-to-one to both Race and Pilot)

## Database Setup
- Uses H2 database with Hibernate configuration
- Includes `MockDataGenerator` for populating test data
- Supports both in-memory and file-based persistence

## Development Notes

### Extensibility
The application is designed with extensibility in mind:
- Interface-based design for business logic
- Clear separation of concerns
- Standard JPA annotations for entity mapping

### Future Enhancements
Potential areas for expansion:
- Complete implementation of team and race management UI
- Add more detailed race statistics
- Implement championship standings calculation
- Add visualization of driver/team performance
- Implement more advanced search/filter capabilities
- Add user authentication and role-based access

## Running the Application
The application can be launched through Maven using the configured JavaFX plugin:
```bash
mvn javafx:run
```

The main entry point is `eus.ehu.presentation.MainWin`. 