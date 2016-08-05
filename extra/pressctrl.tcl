# press CTRL key on boot, this will free a few more KB for BASIC at the cost of disabling the second FDD

proc pressctrl {} {
    after boot { keymatrixdown 6 2; after time 6 "keymatrixup 6 2"; pressctrl}
}
pressctrl
