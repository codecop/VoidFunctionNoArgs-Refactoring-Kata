What did I do?
* Get the code

* Start first test
  * need some kind of approval for all the globals
  * and combinations
  * 25'
* Tweak combinations to get coverage
  * 1 line missing
  * 45'

* Analyse the globals
  * group by read/write usage and
  * identify used constants for each one
  * 30'
  * Could create a graph here, what works with what.
    Could it be done automatically? All names linked to names in same line or vicinity

* Focus on pure Output StellFwd
  * make it a class with its field and
  * abstractions for bit operations
  * 25'

* There is a guard clause using several globals only once
  * extract a policy and we are done
  * 5'

* Do the same with other output NImpuls 10'

* Scan the code and see blocks of working with different values
  * go for blocks with few variables first
  * split IFs to separate value usage
  * when extracting keep the code as is and create duplicate yes/no inverted getters etc.
  * reduce scope to avoid having to deal with globals at once
  * 20'

* Zustand is only used in two places
  * after sorting the methods I see what it does...

OO related approach for StellFwd, NImpuls, SampleZustand etc.
1. extract all methods which deal with the state
2. create static inner class with wrong name
3. pass value of global to constructor, keep name as it is for global (shadowing)
4. instantiate it at beginning of method with short local name "foo"
5. [large step]
   * manually move the methods into the class (which is in same file)
   * fix compile errors by adding "foo." to all calls which do not compile
   * copy back the end value if needed to global at end of procedure
6. clean up class
   * make top level/other place
   * rename good class name, rename good field name, rename good local name
   * simplify methods, e.g. inline negative methods

FP approach with DAO/type/struct
0. choose struct which is a leaf in usage and does not use other structs/data
1. create static inner class with wrong name
2. create struct with fields I want to wrap (local or global), use same names, fields public
   * if fields are only set, make them final and set in constructor/creation
3. instantiate it at beginning of method with short local name "foo"
4. [large step] for each wrapped field
   * search and replace all occurrences X with "foo.X"
   * copy back the end value if needed to global at end of procedure
   * use mark occurrences to see usage, search for usage in end (verify all replaced)
5. reduce scope of new class instance
6. clean up struct
   * make top level/other place
   * rename good class name, rename good field name, rename good local name
     make local name like the prefix, mane field names the suffixes if it had prefix
7. extract function which will also use the struct
   * make struct first parameter, named self
8. recurse
   * maybe create nested structs
   * simplify calls using the nested structs

* Zwsp is easy as it is local.
  * But I cannot extract it because it still uses globals.
  * all initial values get an extra struct
  * recurse until it can move as a whole.
  * Istw came before, Zustand was already done
  * this is more interlinked as I want to extract methods later, not too early
  * 60'
