
# ADD YOUR README UNDER THIS SECTION
Below are tables showing the run times of different tests using different cache sizes for both GeneBankCreateBTree.java and GeneBankSearchBTree.java.
* All tests were run on a student computer with an auto calculated optimal degree and a sequence length of 7.
* Times are approximate +/- 0.05 sec.
* Test4 is blank because the Test4 file doesn't contain any legitimate DNA data that can be read by this program.

    
# GeneBankCreateBTree

| Cache Size ->  |  No Cache  |    100    |    500    |
|:---------------|:----------:|:---------:|:---------:|
| Test1          | 0.184 sec  | 0.080 sec | 0.072 sec |
| Test2          | 0.230 sec  | 0.088 sec | 0.083 sec |
| Test3          | 0.266 sec  | 0.093 sec | 0.086 sec |
| Test4          |            |           |           |
| Test5          | 15.633 sec | 3.050 sec | 1.400 sec |

# GeneBandSearchBTree

| Cache Size ->  |  No Cache   |     100     |     500     |
|:---------------|:-----------:|:-----------:|:-----------:|
| Test1          |  0.250 sec  |  0.235 sec  |  0.216 sec  |
| Test2          |  0.234 sec  |  0.238 sec  |  0.215 sec  |
| Test3          |  0.270 sec  |  0.230 sec  |  0.223 sec  |
| Test4          |             |             |             |
| Test5          |  0.353 sec  |  0.276 sec  |  0.286 sec  |

# Notes on cache speed and usage
When using the cache you can see a significant reduction in the time it takes to create the BTree, especially on Test5.
Increasing the size of the cache makes it run a little faster, but there are definitely diminishing returns the bigger you make the cache.
This is because when using the 500 size cache, the first 100 elements of the cache are still the most frequently accessed nodes like a cache size of 100.
The bigger the cache gets, the more it starts to work like there is no cache at all. Although there are less diskReads and diskWrites when using a larger cache,
there are more elements being stored in the cache which takes longer to search through when looking for a specific node.

# BTree file layout
The BTree file that is created after running GeneBankCreateBTree has two main parts.
* The first part is the metadata needed in order to tell GeneBankSearchBTree how to parse the file in order to create the BTree nodes needed to create the BTree.
  * The entire metadata section is contained in the first 16 bytes of the file.
    * The first 8 bytes are reserved for writing the location of the root node.
    * The next 4 bytes are reserved for writing the sequence length that is used.
    * The last 4 bytes are reserved for writing the degree.
* The second part is the rest of the file which is split into chunks that contain the data for each individual BTree node.
  * Each node size varies based on the degree. The formula used to calculate this is "4 + 1 + 12 * (2 * degree - 1) + 8 * (2 * degree)".
    * The 4 at the beginning of the formula is reserved for the number of keys in the node as an int (4 bytes).
    * The 1 is reserved for the leaf status of the node which is written to the Random Access File as a single byte.
    * (2 * degree - 1) is the formula for calculating the number of keys in the node based on the degree.
      * The size of each key is 12 bytes because it only contains a long (8 bytes) and an int (4 bytes), which gets multiplied by the key function.
    * (2 * degree) is the formula for calculating the number of children in the node based on the degree.
      * The size of each child is just a long (8 bytes) because it's just a pointer to another node, which gets multiplied by the child function.
* If the degree was not entered/entered as 0 then an optimal degree is calculated based on the following equation:
  * (4 + 1 + 12 * (2 * degree - 1) + 8 * (2 * degree)) <= 4096 which resulted in approximately 102 

