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