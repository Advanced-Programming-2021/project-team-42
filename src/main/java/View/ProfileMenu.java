package View;

import java.util.regex.Pattern;

public class ProfileMenu extends Menu{
    public ProfileMenu(Menu parentMenu){
        super("Profile Menu" , parentMenu);
        subMenus.put(Pattern.compile("change nickname"), changeNickname());
        subMenus.put(Pattern.compile("change password"), changePassword());
    }

    public Menu changeNickname(){
        return new Menu("Change Nickname", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                super.execute();
            }
        };
    }

    public Menu changePassword(){
        return new Menu("Change Password", this) {
            @Override
            public void show() {
                super.show();
            }

            @Override
            public void execute() {
                super.execute();
            }
        };
    }

    public void show(){}

    public void execute(){}

    public void run(){}
}
