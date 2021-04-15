# Resources Explained

This explains the important files in the resources (/assets/mooresmod to be more specific) folder

## Blockstates

The .json files in blockstates describes how the blocks display based on their values (like facing and power). These files link to the Models folder (and files)

## Models

The .json files in models describes which textures will be used for this block and where (such as sides or top). These files link to the Textures Folder

Inside the Models file are two important files. Block and Item. Files in these for the same blockstate link must have the same name. One links to the Item texture (in inventory), one links to the Block texture (when placed in the world)

## Textures

This contains many PNG files, seperated into block, item and GUI. GUI is used for things like the Moores Machine GUI when the crafting interface is opened.

## Lang

This folder contains one file, en_us.lang. This file sets the names for blocks in the game to be actual words, rather than the in-code names. Other Lang files can be made for other languages.

## Recipes

This folder contains .json files for crafting table recipes. The name's arent important beyond ease for a developer as all recipes are loaded from here.

## MooresMachineRecipes

This folder contains .json files for the moores machine recipes. Again, the names arent important. The format is the same as for normal crafting recipes, just with a 9x9 crafting grid.


# Notes

Understanding these files beyond the information given can be found on many modding support pages, which is why I dont explain every little detail of them.
