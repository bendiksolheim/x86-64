; Fails with exit code 3 on purpose
_main:
  MOV RDI, 1
  MOV RSI, 2
  ADD RDI, RSI

  MOV RAX, 60 ; exit
  SYSCALL