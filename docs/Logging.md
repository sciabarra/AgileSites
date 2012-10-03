# Logging

Logging is implemented by the Log trait, that provided the following methods

- error
- warn
- info
- debug
- trace
- dump

Currently the Logger logs everything and produce output in the console


(TODO)

Implementing the trait Log will create a logger named like the class including his package.

Logging output can be configured using the configurations files IN THE ROOT OF CLASSPATH

(put them in the resources folder)

Files are:

log.prp
log-test.prp

First if it is found then it is used the log-test.prp, otherwise the log.prp

If both files are not found, logger will use slf4j that must be configured separately

(In WCS11g it is stubbed to java.util.Logging so you have to use a logging.properties to configure it)

The format of the configuration file is

<pre>
_file=logfile.log
_level=DUMP
a=TRACE
a.b=DEBUG
a.b.c=INFO
</pre>

_file points to the output file  (in addition to the console, always available)
_level is the level of the top level
 
a, a.b., a.b.c are the levels of the various loggers

