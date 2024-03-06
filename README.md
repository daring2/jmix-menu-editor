# jmix-menu-editor
[![Check](https://github.com/daring2/jmix-menu-editor/actions/workflows/check.yml/badge.svg)](https://github.com/daring2/jmix-menu-editor/actions/workflows/check.yml)

## Overview
The add-on provides support of multiple main menus in Jmix applications. Menus are stored in the database
and can be created and edited at runtime using the administration screens.

Main functions:
- allow to change menu structure and menu items properties (caption, shortcut, icon, etc) using the administration screens 
at runtime.
- allow to use multiple main menus with different structure for different use groups

Use cases:
- administrators need to change the main menu at runtime without development and deployment of a new application version.
- different user groups need main menus with different structure, these menus must be configurable.

Note: the add-on doesn't change user permissions so visibility of menu items depends on the roles and permissions
assigned to the user. 

## Installation
Install the add-on using the following coordinates `'io.github.it-syn:jmix-menu-editor-starter:<add-on version>'`
according [the platform documentation](https://docs.jmix.io/jmix/studio/marketplace.html).

The add-on versions:

| Jmix Version | Add-on Version                                                                         |
|--------------|----------------------------------------------------------------------------------------|
| 1.4.x        | [0.8.5](https://central.sonatype.com/artifact/io.github.it-syn/jmix-menu-editor/0.8.5) |
| 1.5.x        | [0.8.6](https://central.sonatype.com/artifact/io.github.it-syn/jmix-menu-editor/0.8.6) |
| 2.1.x        | [0.9.1](https://central.sonatype.com/artifact/io.github.it-syn/jmix-menu-editor/0.9.1) |

## Getting started

Let's consider that we want to change the main menu for the administrators of our application (users with the 
'Full access' role).

1. Start the application and login as administrator.
2. Open the 'Main menus' screen.
3. Create a new menu (click the 'Create' button).
4. Specify the menu properties: Name - "admin menu", Role code - "Full Access".
5. Use the "Reset" action in the right section of the screen to load default menu items from `menu.xml`.
6. Make some changes to the menu structure. For example:
    - move or reorder some items using Drag & Drop function;
    - change item captions (click the "Edit" button and change the "Caption key" property);
    - remove some items;
7. Save the changes and close the menu editor.
8. Click the "Apply" button to apply the changes.

You will see that the main menu has reloaded and contains the changes made earlier.

## Usage
Menus can be created and edited in the "Main menus" screen in the Administration menu. Menu attributes (name, description, etc)
and items can be changed in the "Menu editor" screen. Items can be reordered using Drag & Drop function.

![image](https://user-images.githubusercontent.com/11490702/211078842-3d3eba44-fb3a-4a4d-a98f-54ef3dcf8b95.png)

![image](https://user-images.githubusercontent.com/11490702/211079118-cd1b182c-2b75-4c1f-974d-dd84076fb764.png)

![image](https://user-images.githubusercontent.com/11490702/211079299-80e010db-22ae-46fd-8e6e-7fcc36c28843.png)

The Reset action in the "Menu editor" screen restores menu items from the default menu `menu.xml`.

The menu can be assigned to a user role using the "Role code" attribute. After that it is applied for all users
that have this role. If multiple menus are applied via different roles to the user the menu with
the highest Priority attribute is used.
