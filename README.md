# Resistance Calculator Tool v1.0.2

This tool can be used to calculate the most optimal resistor to get from an input voltage to one or more output voltages.
The goal is to provide a tool that solves this and many other similar mathematical problems in electrical engineering.

### latest fixes:
- fixed possible resistor values
- added an "about" page
- added a few tests (WIP)

### future goals:

- check github if a new version is available
- support multiple languages
- more unit tests

### problems:
- in some cases using lower e series can lead to a more accurate result.
- to my knowledge the e series of preferred numbers cannot be calculated mathematically. I came to this conclusion through occasions like:
  * wikipedia and a majority of different sites blatantly rounding 1,4640 to 1,47  and omitting further explanation
  * other sites explaining that you "round generously" without further explanation
  * my own experiments trying to find increments of factors to maybe stumble on what values exactly "generous" should be.

After a while I gave up and was forced to hardcode them. For absolutely noones pleasure I've attached a formatted version as a text document

### Calculation Process:
Given an input voltage (voltIn), desired outputs (voltOut) and either a value or a range of currents:

1. Calculate optimal total resistance via voltIn/current
2. Calculate ratio between voltIn and voltOuts and translate it to theoretical best resistance values
3. find closest resistor using the e series of preferred numbers
4. shift ratios to accommodate for differences between optimal and actual resistance
5. repeat step 3. and 4. until all resistors have been found
6. calculate deviation between voltOut and actual outputs and rank it accordingly

If the user inputs a range of currents, the program calculates 1000 resistor chains and displays the best 10.
If the user inputs one current, the program calculates a resistor chain using this value.

