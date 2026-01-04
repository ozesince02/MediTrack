# Design Decisions

## Package naming: `interfaces` vs `interface`
Java keywords cannot be used as identifiers in package declarations. Since `interface` is a Java keyword, this project uses:
- `com.airtribe.meditrack.interfaces` for `Searchable` and `Payable`

## Storage
- In-memory stores are backed by a generic `DataStore<T>` wrapper over `Map<String, T>`.

## IDs
- IDs are generated using a Singleton `IdGenerator` backed by `AtomicInteger`.

## Bonus: Design Patterns implemented
- Singleton: `com.airtribe.meditrack.util.IdGenerator`
- Factory: `com.airtribe.meditrack.patterns.billing.BillFactory`
- Strategy: `com.airtribe.meditrack.patterns.billing.BillingStrategy` (+ implementations)

## Bonus: AI feature
- `com.airtribe.meditrack.util.AIHelper` provides rule-based specialization + doctor + slot suggestions.


