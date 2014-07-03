<form id="loginForm" action="javascript:void(0);" onsubmit="sendLogin();">
     <fieldset id="inputs">
        <div class="input-group">
            <span class="input-icon"><i class="fa fa-user fa-fw"></i></span>
            <input id="username" class="input-control" type="text" placeholder="Username" autocomplete="on" autofocus required>
        </div>
        <div class="input-group">
            <span class="input-icon"><i class="fa fa-key fa-flip-horizontal fa-fw"></i></span>
            <input id="password" class="input-control" type="password" placeholder="Password" autocomplete="on" required>
        </div>
    </fieldset>
    <fieldset id="actions">
        <input type="submit" class="genericBtn" id="submit" value="Log in">
        <a href="javascript:void(0);" onclick="showForgotPwdForm();">Password dimenticata?</a>
        <a href="javascript:void(0);" onclick="getRegisterForm();">Registrati</a>
    </fieldset>
    
</form>