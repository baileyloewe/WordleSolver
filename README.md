# WordleSolver

A quick, simple command line program project I made to solve Wordles after a non-technical friend thought it wouldn't be possible.

This took a few hours and, while not the perfect example of clean code, project structure, or programming paradigms and discipline, does the job to a solid degree and often easily solves the daily wordle. 

Some takeaways from this project:

1. It could use some clean up. It's decent and gets the job done, but has a lot of flaws in logic and structure. Even my IDE, Intellij, spots quite a few of them. I particularly dislike one of the if statements that is extremely wordy and checks 4 conditions at once. 
2. There are tons of other basic errors, such as pointless conditional checks, and poorly designed algorithms like the triple nested loops with O(n^3) time complexity, etc. If I were to go back to this I would want to work on these.
3. The solving/suggesting logic isn't very good. Right now I essentially weigh letters by how common they are in English and assign "scores" to words based on that. While this gets the job done, it's not especially impressive when you only have around 12,000 possible words to choose from. Some mildly more advanced logic would go a long way into taking this from decent to excellent.
