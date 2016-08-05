# press SHIFT key on boot, this will free a few more KB for BASIC at the cost of disabling the FDD

proc pressshift {} {
    after boot { keymatrixdown 6 1; after time 6 "keymatrixup 6 1"; pressshift}
}
pressshift
