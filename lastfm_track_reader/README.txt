Last.FM Track Reader		(Development since 2011-10-05)

ToDo-List:
		-fix Bugs
		-save to file(serialization)
		-work with MediaMonkey
		-optimize code
		-display progress
		-documentation (uml,...)
		-design patterns(Parser: sigleton; ...)


------------------------------------------------------
History:

0.0.8	|	2011-11-10	|	version with optimized code, and some small new features

New Features:
		-reads last played
		-uses ThreadGroup
		-more statusinformation
		-statusbox is black
		-uses Singleton pattern
		-GUI is usable during dataload
		-includes more exceptions

Bugs:		-bad code					|improvement with next versions
		-no exception handling				|add it ;)
		-not secure against wrong input			|add it ;)

------------------------------------------------------

0.0.7	|	2011-10-17	|	improved multithreaded version

New Features:
		-you can change Number of Threads between 1-4
		-code optimized

Bugs:		-bad code					|improvement with next versions
		-no exception handling				|add it ;)
		-not secure against wrong input			|add it ;)

------------------------------------------------------

0.0.6	|	2011-10-16	|	first multithreaded version

New Features:
		-Multithreading (~100% faster = half working time)

Bugs:		-very bad code (but works^^)			|will be fixed in next version
		-no exception handling				|add it ;)
		-not secure against wrong input			|add it ;)

------------------------------------------------------

0.0.5	|	2011-10-11	|	optimized version

New Features:
		-no more sorting after every new data
		-code optimized

Bugs:		-maybe can be faster
		-no exception handling				|add it ;)
		-not secure against wrong input			|add it ;)

------------------------------------------------------

0.0.4	|	2011-10-10	|	bug fixed version

New Features:
		-correct playcounts (bug fixed)
		-correct sorting	(bug fixed)

Bugs:		-maybe can be faster				|sort vector for better search
		-no exception handling				|add it ;)
		-not secure against wrong input			|add it ;)

------------------------------------------------------

0.0.3	|	2011-10-09	|	better results version

New Features:
		-differences of playcounts are smaller
		-little faster (sorted vector with binary search)
		-detect number of pages automatically

Bugs:		-small differences in playcount
		-maybe can be faster				|sort vector for better search
		-wrong sorting					|use sorted vector
		-no exception handling				|add it ;)
		-not secure against wrong input			|add it ;)

------------------------------------------------------

0.0.2	|	2011-10-08	|	First executable Version

New Features:
		-Read tracks from Last.fm with different settings
		-Show tracklist in table
		-GUI

Bugs:		-differences in playcount
		-slow data adding				|sort vector for better search
		-wrong sorting					|use sorted vector
		-no exception handling				|add it ;)
		-not secure against wrong input			|add it ;)
