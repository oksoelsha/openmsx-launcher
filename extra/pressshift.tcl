# press SHIFT key on boot, this will free a few more KB for BASIC at the cost of disabling the FDD

after boot { keymatrixdown 6 1; after time 14 "keymatrixup 6 1" }