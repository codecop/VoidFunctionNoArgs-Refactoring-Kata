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
  * 30'
