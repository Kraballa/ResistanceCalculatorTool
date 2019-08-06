# Resistance Calculator Tool v1.0.3

**DISCLAIMER: This tool was tested manually so there might be errors I'm not yet aware of. 
I would like to guarantee the correctness of its results but I cannot.
Before using any of the results in actual circuits please make sure they are correct. 
I cannot and will not take responsibility for any damages caused by misuse.
For email contact you can reach me at `vinz.corno@web.de`.** 

This tool can be used to calculate the most optimal resistor to get from an input voltage to one or more output voltages.
Another function lets you calculate two resistors based on a ratio and a total resistance value.
The goal is to provide a tool that solves this and many other similar mathematical problems in electrical engineering.

## Update 1.0.3:
This update features almost no visible changes. I've added a link to the GitHub repository and a detail view to the resistance ratio tool.

*What this update* ***does*** *feature:*

A complete rework of basically everything. Bottom line: cleaned up code by a *lot* and rewrote all parts of the application.

In a more detailed manner: I've reworked the central class, the ResistanceChain. It used to be a mess of setters and random pointers to almost unrelated objects.
Now it's a catch-all object that contains the values you put into it. Since this has been a substancial change all other backend classes had to follow.
So I did the same for them all.

The gui has been reworked to work in a way that lets me expand it easily. I extracted the logic that controls the result list view
and the detailed description. It would be better to have it as an actual gui component that can be used within the SceneBuilder
but I haven't figured out how that is supposed to work quite yet. 

For the future it will be exceptionally easy to expand this application with more tools.

### Future goals:

- display the help image `res\ResCalcHelp.png`
- check github if a new version is available
- support multiple languages
- more unit tests

### Problems faced during Development:
To my knowledge the e series of preferred numbers cannot be calculated mathematically. I came to this conclusion through occasions like:
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

