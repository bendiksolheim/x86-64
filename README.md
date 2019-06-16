# x86-64 virtual machine

An attempt to create a simple virtual machine. It can right now run the stuff in `/programs`.

Mostly follows [this blogpost](http://notes.eatonphil.com/emulator-basics-a-stack-and-register-machine.html), but in Kotlin instead of JavaScript

**Disclaimer: I probably would‘t depend on this being correctly implemented.**

## Specification

- Registers are 64 bit
- Memory is a represented as an array of bytes

## Compile programs for execution

`gcc -S -masm=intel -o out.s in.c`