# Search xAd (Under construction)#

A program written in Scala that spawns 10 workers, where each worker 
simultaneously searches a source of random data for the string 'xAd', then 
informs the parent the following data fields:
- elapsed time
- count of bytes read
- status

### Requirements ###

The parent waits on each worker (confined to a timeout, explained below) and 
writes a report to stdout for each worker sorted in descending order by 
- `<elapsed>:`
- `<elapsed> <byte_cnt> <status>`

Where `<elapsed>` is the elapsed time for that worker in ms, `<byte_cnt>` is 
the number of random bytes read to find the target string and `<status>` should 
be one of {`SUCCESS`, `TIMEOUT`, `FAILURE`}. 

`FAILURE` should be reported for any error/exception of the worker and the 
specific error messages should go to `stderr`. 

`TIMEOUT` is reported if that worker exceeds a given time limit, where the 
program should support a command-line option for the timeout value that defaults 
to `60s`. 

If the status is not `SUCCESS`, the `<elapsed>` and `<byte_cnt>` fields will 
be empty.

Exceptions/errors must be caught and handled gracefully. 

The parent should always write a record for each worker and the total elapsed
time of the program should not exceed the timeout limit. 

If a timeout occurs for at least one worker, only those workers that could 
not complete in time should report `TIMEOUT`, other workers may have completed 
in time and should report 
`SUCCESS`. 

A final line of output will show the average time spent per byte read in units 
of your choice where failed/timeout workers will not report stats. 
11 lines of output total to `stdout`.

The package must include a UNIX shell executable file that runs your program 
and responds appropriately to `-h` (and the timeout option), along with build 
instructions if necessary.

### Developing ###

This application is written in Scala with SBT and Akka, using IntelliJ.

### Running the code locally ###

1. Download this repository
3. Enter `sbt run` to run the code.
