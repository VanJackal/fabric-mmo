# Fabric MMO
An mcMMO clone for fabric.

# Commands
All commands for this mod are behind the `/mmo` parent command.

`/mmo xpbar <visible>`
Set whether the xp bar is visible when xp is gained  
visible: True/False

`/mmo notification <mode>`
Set level up notification mode.
- `title`: Show title for notification
- `actionbar`: Show notification on actionbar
- `chat`: Show notifcation as a chat message
- `disabled`: Disable level up notification

`/mmo level [skill]`
Shows the level for a skill, if skill is ommitted shows xp amount for all skills.

`/mmo global-notif <enable>`
Admin command for disabling/enabling notifications for the whole server.



# Config
## Skills
The config for skills is stored in `config/FabricMMO.yaml`, and stores xp multipliers for blocks for each skill and flat modifier for skills like acrobatics.

## MySQL
This mod requires a MySQL instance to run, it will configure the table itself but needs access configured.
The config file should be stored in `config/FabricMMODB.cfg`, and be in this format:
```
<DB URI>
<USERNAME>
<PASSWORD> 
```

eg:
```
jdbc:mysql://localhost/fabricmmo
myusername
mypassword
```

# Skills

## Combat
*Swords, Archery, Crossroads, Tridents, Spears, Maces, Axes, Unarmed*

Leveled up proportionally to the amount of damage you deal to enemies with each of the skills respective weapons

## Resource Gathering
### Mining
XP gained by mining stones and ores with a pickaxe. Ores give more xp than stones.

### Woodcutting
XP gained by mining logs with an axe.

### Excavation
XP gained by mining dirt/shovelables with a shovel. Some blocks give more XP.

### Herbalism
XP gained by gathering flowers, grass, shroomlights, and other plants. XP gained depends on the type of plant gathered

## Other
### Acrobatics
XP is gained based on the fall damage taken.

### Fishing
XP is gained by fishing up fish or treasure, amount depends on the type of treasure or fish.

### Repair
XP is gained based on the durability repaired at an anvil or using mending.

### Taming
XP is gained by taming animals, the amount gained depends on the animal being tamed.


# Roadmap
## Skills
### Weapons
- [x] Swords
- [x] Archery
- [x] Crossbows
- [x] Tridents
- [x] Spears
- [x] Maces
- [x] Axes
- [x] Unarmed
- [ ] TNT
- [ ] Shields

### Tools
- [x] Mining
- [x] Excavation
- [x] Woodcutting

### Other
- [x] Acrobatics
- [ ] Alchemy
- [x] Fishing
- [x] Herbalism
- [x] Repair
- [x] Taming
