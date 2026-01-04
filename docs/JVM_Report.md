# JVM Report

## Class Loader
The **Class Loader Subsystem** loads `.class` bytecode into memory so the JVM can execute it.

### Types of class loaders
- **Bootstrap Class Loader**
  - Loads core Java classes (e.g., `java.lang.*`).
  - Implemented in native code.
- **Platform Class Loader**
  - Loads platform modules (standard libraries that are not strictly “core” bootstrap).
- **Application (System) Class Loader**
  - Loads application classes from the classpath/module-path (your project classes).

### Parent delegation model
When asked to load a class, a class loader typically:
1. Delegates the request to its **parent** first,
2. Only loads the class itself if the parent cannot find it.

Benefits:
- Prevents duplicate loading of core classes
- Improves security by avoiding “fake” core class overrides

### Important terms
- **Class loading**: find and read bytecode
- **Linking**:
  - **Verification**: bytecode safety checks
  - **Preparation**: allocate memory for static fields (default values)
  - **Resolution**: convert symbolic references to direct references
- **Initialization**: run static initializers (`static {}` blocks) and assign static fields

## Runtime Data Areas
At runtime, JVM uses multiple memory areas:

### Heap
- Stores **objects** and arrays (`new` allocations).
- Shared across threads.
- Managed by the garbage collector.

### Java Stacks
- Each thread has its own **stack**.
- Stores stack frames for method calls:
  - local variables
  - operand stack
  - return address / bookkeeping

### Method Area (Metaspace)
- Stores **class metadata**: class structure, method bytecode, constant pool, etc.
- In modern JVMs, this is backed by **Metaspace** (native memory).

### PC Register (Program Counter)
- Each thread has a small area tracking the current instruction being executed.
- Helps the JVM know where to resume after method calls / branch instructions.

### Native Method Stack
- Used when executing **native** (non-Java) methods via JNI.

## Execution Engine
The **Execution Engine** runs bytecode using a mix of interpretation and compilation.

### Interpreter
- Executes bytecode **instruction-by-instruction**.
- Fast startup, slower steady-state performance.

### JIT (Just-In-Time) Compiler
- Detects “hot” (frequently executed) code paths and compiles them to native machine code.
- Improves long-running performance significantly.

### Garbage Collector (high level)
- Automatically reclaims heap memory from objects that are no longer reachable.
- Different GC algorithms exist; all aim to balance throughput vs latency.

## JIT Compiler vs Interpreter
### Interpreter
- **Pros**: quick startup, simple execution
- **Cons**: slower execution for repeated/hot code

### JIT
- **Pros**: high peak performance after warm-up
- **Cons**: needs warm-up time, uses CPU/memory for compilation

## \"Write Once, Run Anywhere\"
Java source code is compiled into **bytecode** (`.class`), which runs on any platform that has a compatible **JVM** implementation.

Flow:
1. `javac` compiles `.java` → `.class` (bytecode)
2. The JVM executes bytecode on Windows/Linux/macOS, etc.

This works because:
- bytecode is platform-independent
- the JVM is platform-specific (each OS/CPU has its own JVM build)


