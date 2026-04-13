---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# HRmanager Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

### AI usage

Qiyang: @qyscode
Extensive use of ChatGPT, Gemini, GitHub Copilot, IntelliJ Copilot to plan and write code (implementation) through auto-complete,  debugging and code-generation.

Ruben: @1rubzz
GitHub Copilot was used as a supplementary tool during development. It was primarily used for auto-completion and assisting with test case construction. Its use was limited to selected components (e.g., DeleteCommand, StatsPanel, and statistics-related classes)

Wei Jie: @emperorgaodi 
Extensive use of IntelliJ IDE autocomplete tool and deepseek AI to plan/write code.

Minh: @moonmertens
Extensive use of Copilot to plan and write code.

Natalia: @petelectron
Claude was used to refine failing tests and non-compiling code. GitHub Copilot was used to review failing PRs.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S2-CS2103T-T13-1/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S2-CS2103T-T13-1/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside components being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-T13-1/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-T13-1/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-T13-1/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-T13-1/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example. Note that the delete command supports multiple indexes (e.g., `delete 1 3 5`), which follows the same parsing and execution pattern shown below, iterating through each index to delete multiple employees.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`. The command can communicate with the `Model` when it is executed (e.g. to delete an employee).
3. If the command implements `ConfirmableCommand` (such as `edit`, `delete`, `clear`, or `exit`), `LogicManager` does not execute it immediately. Instead, it temporarily saves the command as a pending confirmation and prompts the user for a `y`/`n` input.
4. Upon confirming (`y`), the command can communicate with the `Model` when it is executed (e.g. to delete an employee).
5. Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
6. The result of the command execution or confirmation prompt is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-T13-1/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores HRmanager's employee records, i.e. all `Person` objects (which are contained in a `UniquePersonList` object).
* each `Person` stores `Name`, `Phone`, `Email`, `Role`, `Department`, and optional `Tag` values.
* stores the currently selected `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be observed. The UI can be bound to this list so that it updates automatically when the data changes.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-T13-1/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both HRmanager data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Undo feature

The undo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current HRmanager state in its history.
* `VersionedAddressBook#undo()` — Restores the previous HRmanager state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()` and `Model#undoAddressBook()` respectively.

Given below is an example usage scenario and how the undo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial HRmanager state, and the `currentStatePointer` pointing to that single HRmanager state.

<puml src="diagrams/UndoState0.puml" alt="UndoState0" />

Step 2. The user executes `delete 5` command to delete the 5th employee in the HRmanager. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the HRmanager after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted HRmanager state.

<puml src="diagrams/UndoState1.puml" alt="UndoState1" />

Step 3. The user executes `add n/David …​` to add a new employee. The `add` command also calls `Model#commitAddressBook()`, causing another modified HRmanager state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoState2.puml" alt="UndoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the HRmanager state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding an employee was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous HRmanager state, and restores the HRmanager to that state.

<puml src="diagrams/UndoState3.puml" alt="UndoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

Step 5. The user then decides to execute the command `list`. Commands that do not modify the HRmanager, such as `list`, will usually not call `Model#commitAddressBook()` or `Model#undoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoState4.puml" alt="UndoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all HRmanager states after the `currentStatePointer` will be purged. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoState5.puml" alt="UndoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo executes:**

* **Alternative 1 (current choice):** Saves the entire HRmanager.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save an employee being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

### Search feature

The `search` command is implemented by `SearchCommand`, `SearchCommandParser`, and `PersonMatchesKeywordPredicate`.

The flow is as follows:

1. The user enters `search KEYWORD [MORE_KEYWORDS]...`.
2. `AddressBookParser` routes the input to `SearchCommandParser`.
3. `SearchCommandParser` trims the arguments, rejects blank input, splits input by whitespace into keywords, rejects searches with more than 5 keywords, and rejects any keyword longer than 50 characters.
4. If parsing succeeds, `SearchCommand` is created with a `PersonMatchesKeywordPredicate`.
5. `SearchCommand#execute` updates the model's filtered employee list and returns feedback in the form `X employees listed!`.

The sequence diagram below illustrates the interactions within the `Logic` and `Model` components when a user executes a `search` command (e.g. `search ali hr`).

<puml src="diagrams/SearchSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `search ali hr` Command" />

<box type="info" seamless>

**Note:** The lifeline for `SearchCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

Matching behavior:

* search is case-insensitive
* all employee fields are searched (name, phone, email, role, department, tags)
* each keyword is treated as a partial substring match rather than a full-word match
* each keyword is a non-whitespace token (symbols such as `@`, `_`, `-`, and `.` are allowed)
* multiple keywords are combined using `OR` semantics

This means a command such as `search ali hr` returns employees whose fields contain either `ali` or `hr`, regardless of case (for instance, an employee named "Alice", or someone in the "HR" department).

### Statistics Panel

The statistics panel provides real-time workforce metrics displayed permanently on the right side of the application. This feature follows the **Separation of Concerns (SoC)** principle by separating data calculation from UI display.

**Note:** The statistics panel supports three modes: TAG, DEPARTMENT, and ROLE. The displayed labels and statistics update according to the selected mode. All statistics are always computed from the full employee list (not the filtered list), regardless of any active search or filter.

#### Design Overview

The statistics feature consists of three main components:

1. **`Statistics`** (Model layer): A pure data container that calculates statistics from a list of employees. It has no UI dependencies and is easily testable.
2. **`StatisticsService`** (Service layer): Orchestrates the retrieval of statistics by accessing the filtered employee list from `Logic` and creating a `Statistics` object.
3. **`StatsPanel`** (UI layer): A JavaFX component that displays statistics and listens for changes to the employee list to auto-refresh.

<puml src="diagrams/StatsPanelClassDiagram.puml" width="500" />

The class diagram above uses generic field and method names (e.g., `uniqueValueCount`, `mostCommonValue`, etc.) to reflect that the statistics panel is not limited to tags, but also supports department and role statistics. The `StatsPanel` class updates its UI fields and labels based on the current mode (TAG, DEPARTMENT, or ROLE).

#### Implementation Details

**Statistics Class:**
- Takes a `List<Person>` as input
- Calculates total employees, unique tags, most common tag, and tag distribution
- Uses helper methods `findMostCommonTag()` and `createTagDistribution()` for clean separation
- All fields are final and assigned in the constructor

**StatisticsService Class:**
- Acts as a bridge between `Logic` and `Statistics`
- Provides `getCurrentStatistics()` which converts `ObservableList<Person>` to `List<Person>`
- Uses logging to track statistics calculation events

**StatsPanel Class:**
- Extends `UiPart<Region>` with FXML layout
- Listens to `logic.getAddressBook().getPersonList()` for changes (the full, unfiltered list)
- Updates UI labels when the employee list changes
- Only handles UI updates - no calculation logic
- Supports three dashboard modes: TAG, DEPARTMENT, and ROLE. The displayed statistics and labels update according to the selected mode.

<box type="info" seamless>

**Note:** The statistics panel is intentionally designed to reflect statistics for all employees in the HRmanager, regardless of any active search filters or on-screen filtering. This matches the User Guide and actual implementation. The panel's fields and methods are generic to support all three modes.

</box>

#### Sequence Diagram

The sequence diagram below shows how the statistics panel updates when an employee is added:

<puml src="diagrams/StatsUpdateSequenceDiagram.puml" width="600" />

1. User executes `add` command
2. `LogicManager` executes the command and updates the model
3. `ObservableList` change triggers the listener in `StatsPanel`
4. `StatsPanel` calls `refresh()` → `statisticsService.getCurrentStatistics()`
5. `StatisticsService` creates a new `Statistics` object from the employee list
6. `StatsPanel` updates its labels with the new statistics

#### Design Considerations

**Aspect: Where to place statistics calculation logic**

- **Alternative 1 (current):** Separate `Statistics` class for calculation
    - Pros: Easy to test, follows SRP, reusable
    - Cons: Additional class to maintain

- **Alternative 2:** Calculate statistics directly in `StatsPanel`
    - Pros: Fewer classes
    - Cons: UI layer contains business logic, harder to test

**Aspect: Auto-refresh mechanism**

- **Current choice:** Listener on `ObservableList<Person>`
    - Pros: Updates automatically on any change, no command needed
    - Cons: More complex setup

- **Alternative:** User must type `stats` command
    - Pros: Simpler to implement
    - Cons: Requires manual refresh, less convenient

#### Testing Strategy

- `StatisticsTest`: Unit tests for calculation logic with various employee lists
- `StatisticsServiceTest`: Tests service layer with temporary storage
- `StatisticsServiceIntegrationTest`: Integration tests with actual commands

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Human Resource Manager at a small company (up to 200 employees)
* needs to store and manage employee contact details and basic HR information (not intended for large enterprises or complex HR processes)
* prefers desktop apps over web/mobile alternatives
* is reasonably comfortable using CLI apps
* does not require complex HR features (e.g., payroll, leave management)
* can type fast

**Value proposition**:
HRmanager enables small organizations to efficiently manage and quickly access employee contact details, roles, departments, and tags through a fast, keyboard-driven interface—providing lightweight, statistics-driven HR management for up to 200 employees, without the complexity of enterprise HR systems.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                         | I want to …​                                                                     | So that I can…​                                            |
|----------|---------------------------------|----------------------------------------------------------------------------------|------------------------------------------------------------|
| `* * *`  | new user                        | have an informative beginner friendly user guide                                 | understand the layout and get started quickly              |
| `* * *`  | user                            | add an employee                                                                  | keep track of new employees                                |
| `* * *`  | user                            | delete an employee                                                               | clear up data when it is no longer needed                  |
| `* * *`  | user                            | view all employees                                                               | gain a brief overview of everyone in the company           |
| `* * *`  | user                            | store phone numbers and email addresses                                          | contact employees easily                                   |
| `* * *`  | busy user                       | search for employees by keywords                                                 | quickly find relevant staff members                        |
| `* * *`  | forgetful user                  | get prompted to add all compulsory employee data fields (e.g. Phone, email, ...) | refrain from having incomplete employee data               |
| `* * `   | organised user                  | modify employee details                                                          | keep information up to date                                |
| `* * `   | organised user                  | view a statistics panel summarising employee data                                | quickly understand workforce composition and trends        |
| `* * `   | organised user                  | tag employees by departments and roles                                           | categorise them for easy search                            |
| `* * `   | cautious user                   | get confirmation of my work                                                      | not worry that my changes haven't been saved               |
| `* * `   | clumsy user                     | undo my last action                                                              | avoid making mistakes or losing data                       |
| `* * `   | clumsy user                     | automatically save all data locally after closing the app                        | avoid needing to export the data all the time              |
| `* * `   | clueless user                   | see error messages                                                               | correct my mistakes                                        |
| `* * `   | safe user                       | be notified when executing destructive commands like delete                      | prevent accidental deletion and loss of data               |
| `* `     | lazy user                       | cycle through my previous commands                                               | avoid retyping long commands                               |
| `* `     | expert user                     | import employee data                                                             | manage pre-existing details without hand-typing everything |
| `* `     | expert user                     | export employee data                                                             | back up data, support audits and planning                  |
| `* `     | expert user                     | use aliases for a few frequently used commands                                   | type less and work faster                                  |
| `* `     | expert user                     | repeat previous command or similar                                               | enter commands quickly                                     |
| `* `     | expert user                     | bulk delete employees                                                            | efficiently remove employees that have left the company    |
| `* `     | user responsible for reporting	 | use a centralized dashboard                                                      | maintain visibility over workforce and talent pipeline     |


### Use cases

(For all use cases below, the **System** is the `HRmanager` and the **Actor** is the `user`, unless specified otherwise)


**Use case 1 (UC1): Add employee**<br>

**MSS**

1.  User requests to add an employee by adding employee details (`name`, `phone`, `email`, `role`, `department`, optional tags).
2.  System adds an employee to the records.
3.  System displays success message.
    <br> *Use case ends.*

**Extensions**

* 1a. System detects an error (e.g. format/syntax/duplicate error) in the entered data.
    <br></p>
    * 1a1. System displays an error message with the correct format.
    * 1a2. User provides new data.
    <br> *Steps 1a1-1a2 are repeated until the data provided are correct.*
    <br> *Use case resumes from step 2.*


**Use case 2 (UC2): Delete employee**<br>

**MSS**

1. User requests to remove one or more employees by specifying their index numbers in the displayed list.
2. System validates the provided index numbers.
3. System prompts the user for confirmation that they want to execute a deletion.
4. User confirms intent to proceed with the deletion.
5. System removes the corresponding employee records from the system.
6. System displays a success message indicating the number of employees deleted and their names.
    <br> *Use case ends.*

**Extensions**

* 1a. System detects an error (e.g. format/syntax error) in the provided data.
    * 1a1. System displays an error message with the correct format.
      * 1a2. User provides new data until it is in the correct format.
      <br> *Use case resumes from step 2.*<br><br>

* 2a. One or more indexes are invalid (e.g. index exceeds list size).
    * 2a1. System displays an error message indicating the invalid index.
    * 2a2. User modifies the command.
    <br> *Steps 2a1-2a2 are repeated until the index is valid.*
    <br> *Use case resumes from step 2.*<br><br>

* 4a. User decides not to proceed with the deletion.
    * 4a1. System displays a response indicating that the command was aborted.
    <br> *Use case ends.*


**Use case 3 (UC3): View employees**<br>

**MSS**

1. User requests to view the list of employees.
2. System retrieves the employee records and displays employee list.
   <br> *Use case ends.*

**Extensions**

* 2a. There are no employees stored in the system.
    <br></p>
    * 2a1. System displays an empty employee list.
    <br> *Use case ends*


**Use case 4 (UC4): Search for an employee**<br>

**MSS**

1.  User requests to search for employees using one or more keywords.
2.  System validates the search input.
3.  System processes the search query against the existing employee records.
4.  System displays a filtered list of all employees that match the search.
    Matching is case-insensitive, supports partial substring matching across all fields, and returns employees whose fields contain any of the supplied keywords.
    <br> *Use case ends.*

**Extensions**

* 1a. The user provides a blank search query.
    * 1a1. System displays an invalid command format message together with the proper command usage.
    <br> *Use case resumes from step 1.*<br><br>

* 1b. The user provides invalid input keywords.
    * 1b1. System displays an invalid command format message together with the proper command usage.
    <br> *Use case resumes from step 1.*<br><br>

* 3a. No employees match the provided search query.
    * 3a1. System displays an empty list and a success message.
    <br> *Use case ends.*<br><br>

* 4a. The user then wants to return to the full non-filtered list of employees.
    * 4a1. User requests to view all employees (UC3).
    * 4a2. The system shows the full non-filtered list of employees.
    <br> *Use case ends.*


**Use case 5 (UC5): Edit an employee's details**<br>

**MSS**

1. User requests to edit an employee's details by providing the employee's index in the list alongside the changed details.
2. System prompts the user for confirmation that they want to execute the edit.
3. User confirms intent to proceed with the edit.
4. System edits that employee, replacing the relevant fields with new ones.
5. System displays a success message showing that the employee has been edited, and their new details.
   <br> *Use case ends.*

**Extensions**

* 1a. The user enters the command in the incorrect format.
    * 1a1. System shows an error message, along with the correct format for an edit command.
    <br> *Use case resumes from step 1.*<br><br>

* 1b. The given index is invalid.
    * 1b1. System shows an error message.
    * 1b2. The user modifies the command.
    <br> *Steps 1b1-1b2 are repeated until the index is valid.*
    <br> *Use case resumes from step 2.*<br><br>

* 1c. The user's proposed details are invalid.
    * 1c1. The system shows an error message for the relevant field for which the input restrictions are not adhered to.
    * 1c2. The user modifies the inputs.
    <br> *Steps 1c1-1c2 are repeated until the all the proposed parameters are accepted.*
    <br> *Use case resumes from step 2.*<br><br>

* 1d. User provides empty details.
    * 1d1. System shows an error message.
    <br> *Use case resumes from step 1.*<br><br>

* 3a. User decides not to proceed with the edit.
    * 3a1. System displays a response indicating that the command was aborted.
    <br> *Use case ends.*<br><br>


**Use case 6 (UC6): Cycle through previous executed commands**<br>

**MSS**

1.  User requests to {edit an employee's phone number}*. (Edit is an example. It can be any command)
2.  System {edits the employee's phone number in the records}.
3.  System {displays confirmation message}. (If the command is confirmable)
4.  User suddenly recalls that they have forgotten to also edit the employee's email address.
5.  User requests to cycle to the previous command.
6.  System prefills the input line with the most recently executed command (from step 1).
7.  User modifies the command to edit the {email details} and executes the command.
8.  System {edits the employee's email address in the records}.
    <br> *Use case ends.*

**Extensions**

* 2a. User has entered more than 10 unique commands.
    * 2a1. The oldest command is discarded and can no longer be cycled through.
    <br> *Use case ends.*<br><br>

* 5a. There are no previous successfully executed commands.
    * 5a1. System does not respond to the user's cycle request.
    <br> *Use case ends.*<br><br>

* 6a. The user has executed multiple commands before the recent one, and requests to cycle further back.
    * 6a1. System continues to cycle through older executed commands. If there is already an input, it is saved.
    * 6a2. User stops cycling at their desired past command or cycles forward to get back to a more recent or original command.
    <br> *Use case resumes from step 7.*<br><br>


**Use case 7 (UC7): Importing employee data**<br>

**MSS**

1. User requests to import employee data from destination of choice.
2. System resolves path and checks validity.
3. System converts csv data into list of employees.
4. System asks for user's confirmation. 
5. User confirms decision to import.
6. System saves list of employees, overwriting any pre-existing employee data.
7. System displays a success message indicating the number of employees imported and the file used.
   <br> *Use case ends.*

**Extensions**

* 2a. User input filepath is invalid.
  * 2a1. System displays an error message.
  <br> *Use case resumes from Step 1.*<br><br>

* 3a. File data is invalid (e.g. missing required header rows, duplicate persons).
  * 3a1. System displays an error message.
  <br> *Use case resumes from step 1.*<br><br>

* 4a. User cancels import.
  <br> *Use case ends.*


**Use case 8 (UC8): Exporting current employee data**<br>

**MSS**

1. User requests to export current employee data into destination of choice.
2. System resolves path and checks validity.
3. System converts app data into csv format.
4. System saves csv file into target destination.
5. System displays a success message indicating the file destination.
   <br> *Use case ends.*

**Extensions**

* 2a. User input filepath is invalid.
    * 2a1. System displays an error message.
    <br> *Use case resumes from Step 1.*<br><br>

* 2b. File already exists at target destination.
    * 2b1. System displays an error message.
    <br> *Use case resumes from step 1.*

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. The system should respond to user commands within **1 second** under normal usage conditions.
3. The system should be able to store and manage at least **100 employee records** while maintaining command response times within **1 second**.
5. The system should be usable by **HR managers who are not highly technical**, meaning commands should be simple and documentation should clearly explain how to use them.
6. The system should follow **standard Java coding conventions and modular design principles** to ensure maintainability.
7. The system should ensure that employee data stored in the system remains consistent and is not corrupted during normal usage.
8. The system should ensure that employee information stored locally is not transmitted over the network without user intent.
9. The system should remain stable when invalid commands or inputs are entered and should not crash during normal usage.
10. The system should be packaged as a single executable JAR file so that users can run the application without additional installation steps beyond having Java installed.
11. The system inputs should be primarily through CLI with minimal reliance on mouse interactions, to cater to users who prefer keyboard-driven interfaces.

### Glossary

* **HRmanager**: The name of our desktop application for managing employee records.
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Employee Record**: A collection of information stored in the system about an employee, such as name, email, phone number, role, and department.
* **Command Line Interface (CLI)**: A text-based interface where users interact with the application by typing commands.
* **Department**: The organizational unit an employee belongs to (e.g., `Engineering`, `Finance`, `Human Resources`).
* **Role**: The position or job title of an employee (e.g., `Software Engineer`, `HR Manager`).
* **Alias**: A user-defined shortcut for a command (e.g., `d` as an alias for `delete`).
* **CSV**: Comma-Separated Values, a common file format for storing tabular data.
* **JSON**: JavaScript Object Notation, a common file format for storing structured data.
* **JAR**: Java Archive, a package file format used to aggregate many Java class files and associated metadata and resources into one file for distribution.
* **Prefix**: A specific string used in command syntax to indicate the type of data being entered (e.g., `n/` for name, `p/` for phone).
* **Preamble**: The part of the command before the first valid prefix. It should be empty or just whitespace for a valid command.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

> **Note:** These instructions only provide a starting point for testers to work on; testers are expected to do more *exploratory* testing.

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding an employee

1. Adding an employee, along with their details.

    1. Prerequisites: List all employees using the `list` command. There are no existing employees in the list. Otherwise, use `clear`.

    2. Test case: `add n/Bob Choo p/22222222 e/bob@example.com r/Head of Office d/Operations t/friend` (Valid entry)<br>
       Expected: The employee is added. The success message is shown, along with the added details.

    3. Test case: `add k n/Amy Choo p/22222222 e/amy@example.com r/Head of Office d/Operations t/friend` (Preamble is not a space)<br>
       Expected: The employee is not added. Error message for invalid command format, along with an example of the correct format, shown.

    4. Test case: `add n/Bob Choo p/11111111 e/bob@example.com r/Head of Operations d/Operations t/friend` (Same exact name with existing entry, despite different details)<br>
       Expected: The employee is not added. Duplicate error message is shown, indicating the employee (with same name) already exists.

    5. Test case: `add n/bob Choo p/11111111 e/bob@example.com r/Head of Operations d/Operations t/friend` (Same exact name with different case. Name check is case insensitive.)<br>
       Expected: The employee is not added. Duplicate error message is shown, indicating the employee (with same name) already exists.

    6. Test case: `add n/Lance Choo p/33333333 e/lance@example.com r/Head of HR d/Human Resources t/friend t/friend t/husband` (Multiple tags)<br>
       Expected: The employee is added. The success message is shown, along with the added details. Note that duplicate tags are accepted as one tag.

    7. Test case: `add n/Justin p/33333333 e/lance@example.com r/Head of HR d/Human Resources` (No tags since they are optional)<br>
       Expected: The employee is added. The success message is shown, along with the added details.

    8. Test case: `add n/Amy Cho n/Bob Cho p/11111111 e/bob@meme.com r/Head of Operations d/Operations t/friend` (Two names))<br>
       Expected: The employee is not added. Error messages for duplicated prefix shown.

    9. Other incorrect commands with duplicated attributes for the same employee: `add <other details> p/11111111 p/22222222`, `add <other details> e/amy@example.com e/bob@example.com`, or similar<br>
       Expected: The employee is not added. Error messages for duplicated prefix shown.

    10. Test case: `add n/James& p/11111111 e/bob@meme.com r/Head of Operations d/Operations t/friend` (Invalid name)<br>
       Expected: The employee is not added. The correct format for a valid name is shown.

    11. Other incorrect commands with invalid data: `add <other details> p/abc` (Non-numeric phone number) or similar<br>
        Expected: The employee is not added. The correct format for the attribute for which the argument is invalid is shown.

    12. Test case: `add n/Peppa Pig e/peppa@example.com r/Head of Media d/Media` (No phone number) or similar absence of necessary attributes <br>
        Expected: The employee is not added. Error message is shown, along with the correct format and required parameters for add.

    13. Other incorrect delete commands to try: `add`, `add johndoe p/3333` (no prefix), and other commands which deviate from the command format<br>
        Expected: Similar to previous.

### Deleting an employee

1. Deleting one or more employees while all employees are being shown. NOTE: If the command is valid, the confirmation feature is first triggered. The tester enters 'y' to proceed.

   1. Prerequisites: List all employee using the `list` command. Before each test cases, the number of employees in the (filtered) list is more than or equal to the number of valid arguments given.

   2. Test case: `delete 1`<br>
      Expected: First employee is deleted from the list. Name of the deleted contact shown in the status message.

   3. Test case: `delete 1 3 5`<br>
      Expected: 1st, 3rd, and 5th employees are deleted from the list. Success message shows the names of the deleted employees.

   4. Test case: `delete 1 1 2`<br>
      Expected: Only 2 unique employees are deleted (duplicate index filtered out). Success message shows the names of the deleted employees. Only the 1st and 2nd persons are removed.

   5. Test case: `delete 2 1 3`<br>
      Expected: 1st, 2nd, and 3rd employees are deleted (regardless of order provided). Success message shows the names of the deleted employees. Deletion performed from highest to lowest index to prevent index shifting errors.

   6. Test case: `delete 1 2 999` (Prerequisite: there are less than 999 entries/employees) <br>
      Expected: No employee is deleted. Error details shown for an invalid index (because of 999).

   7. All test cases, except `del` is used instead of `delete`. E.g. `del 1`<br>
      Expected: Same exact behaviour as `delete`

   8. Test case: `delete -3`, `delete a`, `del 0` or similar (No index provided or index is invalid) <br>
      Expected: No employee is deleted. Error message shows that the employee index provided is invalid.

   9. Test case: `delete 1 2 3 4 5 6 7 8 9 10`<br>
      Expected: 10 employees deleted. Success message shows the names of the deleted employees.

   10. Test case: `delete 1 2 3 4 5 6 7 8 9 10 11`<br>
      Expected: No employee is deleted. Error shown: "Too many indexes specified."

   11. Other incorrect delete commands to try: `delete x` (where x is larger than the list size), `del 1 2 a`, `del a`, `del 1 ` (trailing whitespace), `del #`, etc.<br>
      Expected: Similar error handling to above.

2. Deleting an employee from a filtered list (search results)

   1. Prerequisites: Execute `search KEYWORD` to filter the list (where KEYWORD returns one or more matching results based on searchable fields). Multiple search results shown.

   2. Test case: `delete 1`<br>
      Expected: The 1st employee in the filtered search results is deleted. Success message shown. Filtered list updates automatically.

   3. Test case: `search KEYWORD` (where KEYWORD returns 2 or more matching results) followed by `delete 1 2`<br>
      Expected: The 1st and 2nd employees in the filtered results are deleted. Success message shows the names of the deleted employees. Filtered list updates.

   4. Test case: `search KEYWORD` (where KEYWORD returns no matches) followed by `delete 1`<br>
      Expected: Error message shown for invalid index since filtered list is empty.

   5. Test case: `search KEYWORD` (where original list has 3 entries, KEYWORD returns 1 match) followed by `delete 3`<br>
      Expected: Error message shown for invalid index since the index applies to the filtered list, not full list.

### Searching for employees

1. Searching for employees using one or more keywords

   1. Prerequisites: List all employees using the list command. Multiple employees in the list with various names, phones, emails, roles, departments, and tags.

   2. Test case: `search John`<br>
      Expected: All employees whose fields contain "John" (case-insensitive) are listed. Success message shows the number of employees listed.

   3. Test case: `search 91234567`<br>
      Expected: All employees whose fields contain "91234567" are listed. Success message shows the number of employees listed.

   4. Test case: `search example`<br>
      Expected: All employees whose fields contain "example" are listed. Success message shows the number of employees listed.

   5. Test case: `search HR`<br>
      Expected: All employees whose fields contain "HR" are listed. Success message shows the number of employees listed.

   6. Test case: `search ali hr`<br>
      Expected: Employees whose fields contain either "ali" or "hr" are listed. Success message shows the number of employees listed.

   7. Test case: `search @`<br>
      Expected: Search is performed successfully. Employees whose fields contain "@" are listed. Success message shows the number of employees listed.

   8. Test case: `search John_123` (contains underscore)<br>
      Expected: Search is performed successfully because underscore is allowed. Matching employees are listed, or an empty list is shown if there are no matches.

   9. Test case: `search` (no keyword)<br>
      Expected: No search is performed. Error details shown in the error message indicate invalid command format and displays the correct usage format.

   10. Test case: `search ` (blank keyword with spaces)<br>
       Expected: No search is performed. Error details shown in the status message indicating invalid command format and displays the correct usage format.

   11. Test case: `search [a string of 51 characters]`<br>
       Expected: No search is performed. Error details shown due to exceeding 50-character limit.

   12. Test case: `search keyword1 keyword2 keyword3 keyword4 keyword5 keyword6` (more than 5 keywords)<br>
       Expected: No search is performed. Error details shown indicating invalid command format because maximum keywords is exceeded.

   13. Test case: `search keywordThatDoesNotMatchAnyEmployee`<br>
       Expected: Empty list shown. Success message indicates "0 employees listed!".

### Editing an employee

1. Editing an employee's details. NOTE: If the command is valid, the confirmation feature is first triggered. The tester enters 'y' to proceed.

    1. Prerequisites: List all employees using the list command. Employee to edit exists in the list.

    2. Test case: `edit 1 n/Bob Choo p/22222222 e/bob@example.com r/Head of Office d/Marketing t/friend` (Valid entry)<br>
       Expected: The employee is edited. The success message is shown, along with the updated details.

    3. Test case: `edit 1  n/Amy Choo p/22222222 e/amy@example.com r/Head of Office d/Marketing t/friend` (Preamble is a space)<br>
       Expected: The employee is edited. The success message is shown, along with the updated details.

    4. Test case: `edit 1 k n/Amy Choo p/22222222 e/amy@example.com r/Head of Office d/Marketing t/friend` (Preamble is not a space)<br>
       Expected: The employee is not edited. An error message is shown, indicating invalid command format.

    5. Test case: `edit 1 p/!#$!*)**%{` (Invalid field)<br>
       Expected: The employee is not edited. An error message is shown, indicating invalid field value.

    6. Test case: `edit 1 t/friend t/husband` (Multiple tags)<br>
       Expected: The employee is edited. The success message is shown, along with the updated details. All previous tags are deleted, and the employee now only has 'friend' and 'husband' tags.

    7. Test case: `edit 1 t/` (Delete tags)<br>
       Expected: The employee is edited. The success message is shown, along with the updated details. All previous tags are deleted, and the employee has no tags.

    8. Test case: `edit 1 n/Amy Cho n/Bob Choo p/11111111 e/bob@meme.com r/Head of Operations d/Marketing t/friend` (Duplicate fields)<br>
       Expected: The employee is not edited. Error messages for duplicated prefix shown, and the prefix that is duplicated is shown.

    9. Other test cases to try: Test case 8 but with other duplicate fields, e.g. duplicate phone fields or email fields.
       Expected: The employee is not edited. Error messages for duplicated prefix shown, and the prefix that is duplicated is shown.

    10. Test case: `edit 0 n/Amy Cho` (Invalid index) <br>
       Expected: The employee is not edited. An error message is shown, indicating invalid index.

    11. Test case: `edit n/James& p/11111111 e/bob@meme.com` (Invalid name)<br>
       Expected: The employee is not edited. The correct format for a valid name is shown.

    12. Other test cases to try:  Test case 11 but with inputs that do not adhere to other parameters' respective rules. E.g.: `edit 1 p/12!`
       Expected: The employee is not edited. The correct format for the offending parameter is shown.

### Testing the stats panel

1. Viewing the stats panel after editing employee data.

    1. Prerequisites: List all employees using the `list` command. Ensure the stats panel is visible in the UI.

    2. Test case: Edit an employee's department using `edit 1 d/Finance`.<br>
       Expected: The stats panel updates to reflect the new department distribution.

    3. Test case: Edit an employee's role using `edit 1 r/Manager`.<br>
       Expected: The stats panel updates to reflect the new role distribution.

    4. Test case: Edit an employee's tags using `edit 1 t/friend t/colleague`.<br>
       Expected: The stats panel updates to reflect the new tag distribution.

2. Switching dashboard modes (stat command):

    1. Prerequisites: List all employees using the `list` command. Ensure the stats panel is visible in the UI.

    2. Test case: Switch to tag mode using `stat tag` or `stat t`.<br>
       Expected: The stats panel updates to show tag distribution. The panel title/labels reflect tag statistics.

    3. Test case: Switch to department mode using `stat dept`, `stat department`, or `stat d`.<br>
       Expected: The stats panel updates to show department distribution. The panel title/labels reflect department statistics.

    4. Test case: Switch to role mode using `stat role` or `stat r`.<br>
       Expected: The stats panel updates to show role distribution. The panel title/labels reflect role statistics.

    5. Test case: Enter an invalid mode, e.g., `stat xyz`.<br>
       Expected: An error message is shown indicating the command format is invalid. The stats panel remains unchanged.

### Testing Confirmation Prompts

Many commands in HRmanager require confirmation before they execute, to prevent accidental changes. When prompted, entering `y` confirms and executes the command, while entering `n` cancels it.

Test the following for each confirmable command:

1. Editing an employee (edit command)

    1. Test case: Enter a valid edit command (e.g., `edit 1 n/New Name`). When prompted, enter `y`.<br>
       Expected: The employee is edited. Success message is shown.

    2. Test case: Enter a valid edit command (e.g., `edit 1 n/New Name`). When prompted, enter `n`.<br>
       Expected: The edit is cancelled. No changes are made. Cancellation message is shown. <br><br>

2. Deleting an employee (delete command)

    1. Test case: Enter a valid delete command (e.g., `delete 1`). When prompted, enter `y`.<br>
       Expected: The employee is deleted. Success message is shown.

    2. Test case: Enter a valid delete command (e.g., `delete 1`). When prompted, enter `n`.<br>
       Expected: The deletion is cancelled. No changes are made. Cancellation message is shown. <br><br>

3. Clearing all employees (clear command)

    1. Test case: Enter `clear`. When prompted, enter `y`.<br>
       Expected: All employees are deleted. Success message is shown.

    2. Test case: Enter `clear`. When prompted, enter `n`.<br>
       Expected: The clear operation is cancelled. No changes are made. Cancellation message is shown. <br><br>

4. Exiting the application (exit command)

    1. Test case: Enter `exit`. When prompted, enter `y`.<br>
       Expected: The application closes.

    2. Test case: Enter `exit`. When prompted, enter `n`.<br>
       Expected: The application remains open. Cancellation message is shown.

### Testing Undo Workflows

Use these tests to verify generic undo workflows. NOTE: If the command is valid, the confirmation feature is first triggered. The tester enters 'y' to proceed.

1. Edit then undo (tracked command)

    1. Prerequisites: Ensure employee 1 has a known initial field value (e.g., name is `Alice Tan`).

    2. Test case: Enter `edit 1 n/Alice Lim`. Then enter `undo`.<br>
       Expected: The edit is applied first. After `undo`, employee 1's name returns to the initial value (`Alice Tan`).<br><br>

2. Edit, then an untracked command, then undo

    1. Prerequisites: Ensure employee 1 has a known initial field value.

    2. Test case: Enter `edit 1 n/Alice Lim`. Enter a non-data-modifying command (e.g., `list` or `search Alice`). Then run `undo`.<br>
       Expected: Undo skips the untracked command and reverts the latest tracked change. Employee 1's edited field returns to its initial value.<br><br>

3. Delete then undo (tracked command)

    1. Prerequisites: Ensure there is at least one employee in the list.

    2. Test case: Enter `delete 1`. Then enter `undo`.<br>
       Expected: The employee is deleted first. After `undo`, the same employee is restored to the list.

### Testing Command Cycling

1. Command cycling after tracked commands

    1. Prerequisites: Ensure there is at least one employee in the list.

    2. Test case: Enter `add n/Test User p/91234567 e/test@example.com r/Intern d/Operations`, then enter `delete 1` (confirm valid commands with `y` when prompted). Press the non-numpad Up arrow key repeatedly in the command box.<br>
       Expected: The first Up arrow shows the most recent command (`delete 1`). The next Up arrow shows the earlier command (`add n/Test User p/91234567 e/test@example.com r/Intern d/Operations`).

    3. Test case: After reaching the older command using Up arrow, press the non-numpad Down arrow key.<br>
       Expected: Cycling moves forward through command history toward newer commands (back to `delete 1`, then to the original/current input state).

### Importing employee list

NOTE: If the command is valid, the confirmation feature is first triggered. The tester enters 'y' to proceed.

1. Importing employee data, regardless of OS

   1. Test case: `import test.csv` (Valid entry, assuming `test.csv` exists with valid data)<br>
       Expected: Employee list is imported by parsing `test.csv` in HRmanager's home folder, overwriting existing employee data. Success message is shown with number of employees imported and the file path.
   
   2. Test case: `import invalid.csv` (Invalid entry, assuming `invalid.csv` exists, but is in the wrong format)<br>
      Expected: Employee list is not imported. An error message is shown, indicating the first format error of `invalid.csv` encountered by the parser.
   
   3. Test case: `import test.txt` (Invalid entry)<br>
      Expected: Employee list is not imported. An error message is shown, indicating invalid file extension.

2. Importing employee data, on Windows OS

   1. Test case: `import C:\Users\username\Downloads\test.csv` (Valid entry, assuming `test.csv` exists in `Downloads` with valid data)<br>
   Expected: Employee list is imported by parsing `test.csv` in user's `Downloads` folder, overwriting existing employee data. Success message is shown with number of employees imported and the file path.

   2. Test case: `import C:\Users\username\invalid\path\nonexistent\test.csv` (Invalid path)<br>
     Expected: Employee list is not imported. An error message is shown, indicating invalid path.

3. Importing employee data, on MacOS/Linux

   1. Test case: `import /home/user/data.csv` (Valid entry, assuming `test.csv` exists with valid data)<br>
      Expected: Employee list is imported by parsing `test.csv`, overwriting existing employee data. Success message is shown with number of employees imported and the file path.

   2. Test case: `import "/home/user/My Data.csv"` (Valid entry)<br>
      Expected: Employee list is imported by parsing `My Data.csv`, overwriting existing employee data. Success message is shown with number of employees imported and the file path.

   3. Test case: `import /home/user/My Data.csv` (Invalid entry, no quotes around path containing space(s))<br>
      Expected: Employee list is not imported. An error message is shown, indicating invalid path.

### Exporting employee list

1. Exporting current employee data, regardless of OS

   1. Test case: `export employees.csv` (Valid path, assuming no existing employees.csv in HRmanager's home folder)<br>
        Expected: A file `employees.csv` is created in HRmanager's home folder, containing the current employee data in csv format. The success message is shown, with the number of people exported and the file path.

   2. Test case: `export employees.txt` (Invalid path, only csv exports are allowed)<br>
        Expected: No csv file is created. An error message is shown, indicating invalid path.
   
   3. Test case: `export duplicate.csv` (Invalid path, assuming existing duplicate.csv in HRmanager's home folder)<br>
        Expected: No csv file is created. An error message is shown, indicating no overwriting of local files.

2. Exporting current employee data, on Windows OS

   1. Test case: `export C:\Users\username\Downloads\test.csv` (Valid entry, assuming no existing test.csv in directory)<br>
         Expected: A file `test.csv` is created in the User's Downloads folder, containing the current employee data in csv format. The success message is shown, with the number of people exported and the file path.

   2. Test case: `export C:\Users\username\Downloads\new\path\employees.csv` (Valid entry, assuming no existing subdirectory `new`)<br>
      Expected: A directory `new` and a subdirectory `path` are created inside User's Downloads folder, a file employees.csv containing the current employee data in csv format is created inside. The success message is shown, with the number of people exported and the file path.

3. Exporting current employee data, on MacOs/Linux

    1. Test case: `export /home/user/data.csv` (Valid entry, assuming no existing data.csv in directory)<br>
       Expected: A file `data.csv` is created in the directory, containing the current employee data in csv format. The success message is shown, with the number of people exported and the file path.

    2. Test case: `export "/home/user/My Data.csv"` (Valid entry)<br>
       Expected: A file `My Data.csv` is created in the directory, containing the current employee data in csv format. The success message is shown, with the number of people exported and the file path.
   
    3. Test case: `export /home/user/My Data.csv` (Invalid entry, no quotes around path containing space(s))<br>
       Expected: No csv file is created. An error message is shown, indicating invalid path.

### Saving and loading data

#### Dealing with corrupted data files

**Prerequisites:**
- In the data folder, have a `HRmanager.json` file with at least one employee entry.

**Test case 1:**
Open `HRmanager.json` and modify an employee so that a mandatory field (e.g., name, phone, email, role, department) is set to an empty string: `""`. Start up the application.
<br><br>
**Expected:**
- The employee list will appear blank in the UI.
- A warning message is shown to the user.
- The corrupted `HRmanager.json` file is not overwritten on exit or window close; it remains on disk in its corrupted state.
- If you want to recover your data, you must manually edit the JSON file to correct the errors. If you execute any data-modifying command (e.g., add, delete, edit), the corrupted file will be overwritten with the current (possibly empty) data, and your previous data will be lost.

**Test case 2:**
Delete a mandatory field from an employee entry, or insert an invalid value (e.g., special characters in name, malformed email). Start up the application.
<br><br>
**Expected:**
- Similar to previous: blank list, warning message, file not overwritten until a data-modifying command is executed.

#### Dealing with missing data files

**Prerequisites:**
- Have an existing `HRmanager.json` file in the data folder.

**Test case 3:**
Delete or rename `HRmanager.json`. Start up the application.
<br><br>
**Expected:**
- The initial sample employee list will appear in the UI.
- The `HRmanager.json` file will be created (with sample entries) only after a data-modifying command (e.g., add, delete, edit) is executed.

### Planned Enhancements

Team Members: 5

1. **Same-Name Support:** Allow adding multiple employees who share the exact same legal name, uniquely identified by other fields such as phone, email, or an auto-generated employee ID (instead of requiring artificial suffixes like "John Doe - Junior").
2. **Unique Contact Validation:** Enforce uniqueness on phone numbers and email addresses to prevent duplicate contact details across employees, while allowing multiple employees to share the same legal name without artificial suffixes.
3. **Non-ASCII Character Support**: Extend data validation to support non-ASCII characters, allowing for diverse and international names.
4. **Support for Phone Extensions**: Update the phone number constraints and formatting to accept different phone extensions.
5. **Increase Maximum Number of Employees**: Optimize the underlying storage and UI to smoothly handle a significantly larger database of employees.
6. **Detailed Undo Feedback**: Modify the `undo` command's success message to explicitly state which command was just undone.
7. **Expanded Import/Export Formats**: Support importing and exporting data using other common file formats beyond `.csv` (e.g., `.xlsx`).
8. **Fix Multi-screen Coordinate Bug**: Resolve an issue where opening the app on a single screen, after previously moving it to a secondary screen, causes the GUI to open off-screen.
9. **Increase Undo Limit**: Increase the capacity of the undo history queue to allow users to revert more previous commands.

## **Appendix: Effort**

The development of HRmanager required considerable effort which extended well beyond AddressBook-Level3's baseline (AB3). Whilst AB3 provided a robust foundation, we have added numerous specific enhancements aimed at improving our userbase (Human Resource Managers') experience.
This appendix evaluates the effort our team invested and how our project evolved from the initial AB3 codebase.

### 1. Stats Panel Integration
One of the biggest changes to the project was the introduction of the Stats Panel. Recognizing that HR professionals need to see data at a quick glance, we designed a dashboard that aggregates database metrics (e.g., employee counts by department, role distributions) dynamically. 
*   **The Effort:** Building this required tightly coupling the new UI components to the underlying `ObservableList` of employees. We had to implement listeners which recalculate statistics seamlessly whenever the database changes, whilst ensuring that thread safety and performance were not compromised.


### 2. Quality of Life (QoL) Features
To elevate the application from a basic address book to a professional tool, we dedicated considerable time to QoL features that streamline the user experience:
*   **Confirmation Prompts:** For destructive actions (like clearing the database), we implemented a confirmation requirement. This involved modifying the standard command execution loop to pause, wait for a `Y`/`N` input, and hold a temporary state without breaking the overarching Model.
*   **Command Cycling:** We implemented shell-like command history navigation (using up/down arrow keys), which required adding a command history tracker to the UI controller and syncing it with user keystrokes.
*   **Undo Functionality:** Introducing an undo mechanism was a highly complex endeavor. It required us to snapshot the system state before any mutative command, ensuring we could safely revert data without memory leaks or state corruption.

### 3. Import / Export System
While AB3 handles basic background JSON, HR professionals primarily work with spreadsheets. We implemented robust `.csv` Import and Export commands.
*   **The Effort:** This was far more difficult than simple File I/O. We had to build a custom CSV parser and serializer capable of mapping raw strings to our domain-specific objects (like `Department` or `Role`), whilst handling formatting errors (duplicates, missing values, etc.) gracefully. The system had to be capable of bulk-adding entries, validating them on the fly, and rejecting malformed files without corrupting the existing HRmanager database.

