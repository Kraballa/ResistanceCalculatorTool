# Resistance Calculator Tool v1.1

![Resistance Calculator Preview](res/ResCalcPreview.png "Resistance Chain Calculator displaying detailed information on a selected Resistance Chain") 

**DISCLAIMER: This tool was tested manually so there might be errors I'm not yet aware of. 
I would like to guarantee the correctness of its results but I cannot.
Before using any of the results in actual circuits please make sure they are correct. 
I cannot and will not take responsibility for any damages caused by misuse.
For email contact you can reach me at `vinz.corno@web.de`.** 

This tool can be used to calculate the most optimal resistor to get from an input voltage to one or more output voltages.
Another function lets you calculate two resistors based on a ratio and a total resistance value.
The third calculation technique determines 3 resistance values to get the desired thresholds on an inverted comparator with hystheresis.
The goal is to provide a tool that solves this and many other similar mathematical problems in electrical engineering.

## (Current) Update 1.1.1 (including Comparator circuit export):
- calculation results can now be exported as .asc files. Opening them in LTSpice, a freeware circuit simulation program, allows users to simulate them and validate the values. Please make sure all settings are correct (especially with the comparator circuit)
It started out as a context menu but since those aren't always visible I've replaced them with a button below the detailed description.
- added shortcuts to some menu options

### (Previous) Update 1.1:
New Update. The big part is a new type of calculation that deals with inverted comparators with hystheresis. The calculation process is long and sort of expensive.
After an initial approximation values around those guesses are selected, in 3 dimensions. In the UI the TextArea titled "amount" corresponds to the amount of values that are picked for each dimension,
meaning if it's set too high it will just take minutes and finally crash. With the tested inputs there were no improvements of the answers after 80 and past 100 the calculation took way too long (100^3 = 1.000.000 times a bunch of maths).
For now the suggested value is the default, 60. Expect improvements in the future. 
I'm especially looking into one value that could potentially increase the accuracy of the guess by a few hundred percent. Enough of that though.

Along with a few major and minor tweaks to the UI comes a new visualization of the Resistance Chains. 
It shows input voltage and ampere, resistance values and for more comfortable use, the actual output inbetween them.
I'm somewhat torn on its usefulness because it displays the same information that's already on screen, 
I'll just have to wait for feedback on it. 
It displays all relevant informations to turn the calculated Resistance Chain into an actual circuit in a compact immediately understood way
which is why I implemented it.

UI tweaks include:
- added mnemonics. Press alt and the program will underline which key press opens which menu. For example, pressing `alt` then `n` opens the `New` menu and pressing `c` after that opens a new Resistance Chain Calculator Tab.
- the colours used to visually represent a too large diversion from desired values in the detailed list is also used for that specific cell in the Resistance Chain list
- tweaked ui element margins for more consistency
- added more details to the ratio calculator panel
- the detailed view is now a TextArea, which allows copying its contents
- the detailed view has its values rounded to a certain amount of decimal places. 10 for ampere, 4 for outputs, 2 for all ratios and total resistances

And last but not least as always I rewrote and reformatted a bunch of stuff to improve the overall quality of the application.

### (Previous) Update 1.0.3:
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

- better described inputs
- autosort multiple number inputs
- SI scale units (done for inputs, not yet for outputs)
- options for number of calculations
- rework ListPanels to be generic and move the detailed description methods to their originating classes
- display the help image `res\ResCalcHelp.png`
- check github if a new version is available
- support multiple languages
- more unit tests

### Download:
Simply download `ResistanceCalculatorTool.jar` found in the project root.

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

