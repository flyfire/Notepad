Java Thread
===============
+ ``Thread.sleep()`` interacts with the thread scheduler to put the current thread in wait state for specified period of time. Once the wait time is over, thread state is changed to runnable state and wait for the CPU for further execution. So the actual time that current thread sleep depends on the thread scheduler that is part of operating system.
+ Thread Sleep important points
  + It always pause the current thread execution.
  + The actual time thread sleeps before waking up and start execution depends on system timers and schedulers. For a quiet system, the actual time for sleep is near to the specified sleep time but for a busy system it will be little bit more.
  + Thread sleep doesn’t lose any monitors or locks current thread has acquired.
  + Any other thread can interrupt the current thread in sleep, in that case InterruptedException is thrown.
+ ``Thread.join()``
  + ``public final void join()``,This method puts the current thread on wait until the thread on which it’s called is dead. If the thread is interrupted, it throws InterruptedException.
  + ``public final synchronized void join(long millis)``,This method is used to wait for the thread on which it’s called to be dead or wait for specified milliseconds. Since thread execution depends on OS implementation, it doesn’t guarantee that the current thread will wait only for given time.
  + ``public final synchronized void join(long millis, int nanos)``,This method is used to wait for thread to die for given milliseconds plus nanoseconds.
+ ``Thread Stats``
  + we can create a thread in java and start it but how the thread states change from Runnable to Running to Blocked depends on the OS implementation of thread scheduler and java doesn’t have full control on that.
  + ``New``:When we create a new Thread object using new operator, thread state is New Thread. At this point, thread is not alive and it’s a state internal to Java programming.
  + ``Runnable``:When we call start() function on Thread object, it’s state is changed to Runnable and the control is given to Thread scheduler to finish it’s execution. Whether to run this thread instantly or keep it in runnable thread pool before running it depends on the OS implementation of thread scheduler.
  + ``Running``:When thread is executing, it’s state is changed to Running. Thread scheduler picks one of the thread from the runnable thread pool and change it’s state to Running and CPU starts executing this thread. A thread can change state to Runnable, Dead or Blocked from running state depends on time slicing, thread completion of run() method or waiting for some resources.
  + ``Blocked/Waiting``:A thread can be waiting for other thread to finish using thread join or it can be waiting for some resources to available.
  + ``Dead``:Once the thread finished executing, it’s state is changed to Dead and it’s considered to be not alive.
+ ``wait,notify,notifyAll``
  + The Object class in java contains three final methods that allows threads to communicate about the lock status of a resource. These methods are ``wait()``, ``notify()`` and ``notifyAll()``.The current thread which invokes these methods on any object should have the object monitor else it throws ``java.lang.IllegalMonitorStateException`` exception.
  + ``wait``:Object wait methods has three variance, one which waits indefinitely for any other thread to call notify or notifyAll method on the object to wake up the current thread. Other two variances puts the current thread in wait for specific amount of time before they wake up.
  + ``notify``:notify method wakes up only one thread waiting on the object and that thread starts execution. So if there are multiple threads waiting for an object, this method will wake up only one of them. The choice of the thread to wake depends on the OS implementation of thread management.
  + ``notifyAll``:notifyAll method wakes up all the threads waiting on the object, although which one will process first depends on the OS implementation.
+ ``Thread Safety``
  + Synchronization is the easiest and most widely used tool for thread safety in java.
  + Use of Atomic Wrapper classes from ``java.util.concurrent.atomic`` package.
  + Use of locks from ``java.util.concurrent.locks`` package.
  + Using thread safe collection classes.
  + Using ``volatile`` keyword with variables to make every thread read the data from memory, not read from thread cache.
  + ``Synchronization``
    + Synchronization is the tool using which we can achieve thread safety, JVM guarantees that synchronized code will be executed by only one thread at a time. java keyword synchronized is used to create synchronized code and internally it uses locks on Object or Class to make sure only one thread is executing the synchronized code.
    + Java synchronization works on locking and unlocking of resource, before any thread enters into synchronized code, it has to acquire lock on the Object and when code execution ends, it unlocks the resource that can be locked by other threads. In the mean time other threads are in wait state to lock the synchronized resource.
    + We can use synchronized keyword in two ways, one is to make a complete method synchronized and other way is to create synchronized block.
    + When a method is synchronized, it locks the Object, if method is static it locks the Class, so it’s always best practice to use synchronized block to lock the only sections of method that needs synchronization.
    + While creating synchronized block, we need to provide the resource on which lock will be acquired, it can be XYZ.class or any Object field of the class.
    + synchronized(this) will lock the Object before entering into the synchronized block.
    + You should use the lowest level of locking, for example if there are multiple synchronized block in a class and one of them is locking the Object, then other synchronized blocks will also be not available for execution by other threads. When we lock an Object, it acquires lock on all the fields of the Object.
    + Java Synchronization provides data integrity on the cost of performance, so it should be used only when it’s absolutely necessary.
    + Java Synchronization works only in the same JVM, so if you need to lock some resource in multiple JVM environment, it will not work and you might have to look after some global locking mechanism.
    + Java Synchronization could result in deadlocks.
    + Java synchronized keyword cannot be used for constructors and variables.
    + It is preferable to create a dummy private Object to use for synchronized block, so that it’s reference can’t be changed by any other code.
    + We should not use any object that is maintained in a constant pool, for example String should not be used for synchronization because if any other code is also locking on same String, it will try to acquire lock on the same reference object from String pool and even though both the codes are unrelated, they will lock each other.
    + Notice that lock Object is public and by changing it’s reference, we can execute synchronized block parallel in multiple threads. Similar case is true if you have private Object but have setter method to change it’s reference.
+ ``Daemon Thread``:When we create a Thread in java, by default it’s a user thread and if it’s running JVM will not terminate the program. When a thread is marked as daemon thread, JVM doesn’t wait it to finish and as soon as all the user threads are finished, it terminates the program as well as all the associated daemon threads.
+ ``ThreadLocal``:Java ``ThreadLocal`` is used to create thread-local variables. We know that all threads of an Object share it’s variables, so if the variable is not thread safe, we can use synchronization but if we want to avoid synchronization, we can use ThreadLocal variables.Every thread has it’s own ``ThreadLocal`` variable and they can use it’s ``get()`` and ``set()`` methods to get the default value or change it’s value local to Thread. ``ThreadLocal`` instances are typically private static fields in classes that wish to associate state with a thread.
+ ``Generate Thread Dump``:Find out the PID of the java process using ``ps -eaf | grep java`` command,``jstack PID >> mydumps.tdump``.