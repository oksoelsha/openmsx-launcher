# press CTRL key on boot, this will free a few more KB for BASIC at the cost of disabling the second FDD

after boot { keymatrixdown 6 2; after time 14 "keymatrixup 6 2" }