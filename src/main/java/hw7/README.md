# Discussion

In this experiment, I implemented a HashMap based on both the OpenAddressing technique and the Chaining technique.

I developed the OpenAddressing HashMap by creating an array of Node objects. A node object would be written to an index
in the array based on its hashCode value, assuming this index was not full. There were three primary developer parameters
in this -- the size of the array (number of buckets), the load factor, and the probing strategy. I tweaked these by
setting them as constant fields, and edited them for various experimental runs. Experimenting with the
array size, it appeared smaller array sizes led to improved performance. Furthermore, the load factor should not
be too close to 1, because this significantly hampered performance, since each when our array was almost full, it would
require probing to in effect perform a linear search to find a free index. Lastly, I implemented/experimented with
1) linear probing, 2) quadratic probing, and 3) double probing. Surprisingly, linear probing seemed to work the best.
Both quadratic probing and double probing led to some inaccuracies with insert, because in this case, my code would
not examine every index in the array, and would falsely claim the array was full even when it was not.

On the other hand, I developed the Chaining HashMap with an Array of buckets, where each bucket was an ArrayList of
Node objects (again pertaining to a key/value pair). I chose to implement it in this way -- with ArrayLists rather
than LinkedLists -- out of ease of implementation, as without being able to explicitly iterate to the next Node in
a LinkedList without implementing my own LinkedList class, this way proved easier. I also had to decide on the size
of the array parameter. I chose 3 because it appeared that smaller sizes proved better than larger ones, though I'm not
quite sure why this was. 

In all, the OpenAddressing HashMap proved to be faster than the Chaining HashMap for most sufficiently low load factors.
For instance, for a load factor of 0.5, I found

Benchmark                                                                 (fileName)  Mode  Cnt           Score   Error   Units
JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         305.113           ms/op

for open addressing, and 

Benchmark                                                                 (fileName)  Mode  Cnt           Score   Error   Units
JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         503.017           ms/op

for chaining.

However, note that for a load factor of 0.9, this became

Benchmark                                                                 (fileName)  Mode  Cnt           Score   Error   Units
JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         541.424           ms/op

which reflects worse performace. 


