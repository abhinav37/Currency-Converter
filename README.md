#  Currency Converter
#### Develop a Currency Conversion App that allows a user view exchange rates for any given currency
### Functional Requirements:
- [ ] Exchange rates fetched from: https://currencylayer.com/documentation  
- [ ] Use free API Access Key for using the API
- [ ] User must be able to select a currency from a list of currencies provided by the API(for currencies that are not available, convert them on the app side. When converting, floating-point error is accpetable)
- [ ] User must be able to enter desired amount for selected currency
- [ ] User should then see a list of exchange rates for the selected currency
- [ ] Rates should be persisted locally and refreshed no more frequently than every 30 minutes (to limit bandwidth usage)

## Technologies used
- Kotlin
- Kotlin -extension
- Coroutines
- Retrofit
- Room (for database)
- Material components
- Unit testing (JUnit5)

## On Fork
- you can change the API key as its limited to 250 request/day 
