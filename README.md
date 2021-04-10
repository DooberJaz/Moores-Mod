# Moores-Mod
A Minecraft mod that allows for the creation of various computer components accurate to real life from the logic gate level. Part of my 4th year Masters Dissertation.

For the full experience, download the modpack at this link *link pending* as it has better questing implemented, allowing for a much easier learning experience and better use of the mod.

## Current Work Done

**Logic Gates** - Binary (on/off) logic gates, compatible with redstone. Crafting - while somewhat inaccurate - is simple and can be done with a normal crafting bench

**Blustone** - My custom redstone equivalent. It doesnt lose power when traveling over blocks (but does have to be facing the right way as a caviat, like repeaters). Blue also looks nicer, which is a plus!

**Moores Machine** - My custom crafting bench. It allows for 9x9 crafting, which fascilitates accurate crafting of the blustone components (usually accurate to circuit diagrams). At present, due to the complex and costly crafting required, it does not consume items, though this is subject to change depending on how overpowered it is. There are some bugs remaining with it, coding custom crafting is super difficult, even with the help of other mods.

**Blu Logic Gates** - Logic gates constructed out of blustone. These work based on the power of the blustone going into them, making them capable of performing multi-bit operations. They are constructed in a complex but accurate fashion at the Moores Machine.

**Higher level blu logic** - This is anything from adders to multiplexors to ALUs. All can be crafted and function far better than I expected.

## Future Work

**CPUs** - The final goal of this mod is to have a 1 block CPU within minecraft, though multiple setbacks have impacted this. Additionally, explaining a CPU within the bounds of the game would be a huge challenge. However, one day I'd still like to try.

**Bug fixes** - I still have quite a few bugs, which sucks. Fixing these is a priority currently. Please let me know if you find any bugs.

**Code Documentation** - Yeah I'm just putting this off for now :p

## Explanation of Whats in This Repo

**Java** - The java code for the mod. Contains all the code for blocks, crafting etc. Not yet properly documented.

**Resources** - Textures and texture assignment files, as well as any crafting recipes. Mostly in Json format so have fun with that!

**.jar file** - Hopefully a downloadable file that will allow you to run my mod? Otherwise just get it at [this link](https://www.curseforge.com/minecraft/mc-mods/mooresmod), or get the modpack at this link *link pending*

**.zip file** - The Modpack file created by CurseForge. Importing this zip into your own curseforge client will let you launch and play my mod exactly as I intended! (Additionally it is the most up to date file, I may have missed some bug fixes in this github repo)
