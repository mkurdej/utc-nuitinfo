<?php

require 'facebook-php-sdk/src/facebook.php';
require 'util.php';

// Create our Application instance (replace this with your appId and secret).
// UTC-NuitInfo11
// https://developers.facebook.com/apps/274832242563339/
$facebook = new Facebook(array(
  'appId' => '274832242563339',
  'secret' => '4946a0747eb4c8b07340c856bd6a741a',
));

// Get User ID or 0 if not connected
$user = $facebook->getUser();

// We may or may not have this data based on whether the user is logged in.
//
// If we have a $user id here, it means we know the user is logged into
// Facebook, but we don't know if the access token is valid. An access
// token is invalid if the user logged out of Facebook.

if ($user) {
  try {
    // Proceed knowing you have a logged in user who's authenticated.
    $user_profile = $facebook->api('/me');
  } catch (FacebookApiException $e) {
    error_log($e);
    $user = null;
  }
}

if ($user) {
    echo $user;
    die();
} else {
    $permissionScope = 'user_likes,friends_birthday,friends_likes'; //offline_access
    $loginParams = array(
        scope => $permissionScope,
        redirect_uri => 'http://www.floriandubois.com/nuitinfo/facebook/login.php'
    );
    $loginUrl = $facebook->getLoginUrl($loginParams);
}

?>

<?php if (!$user): ?>
    <!doctype html>
    <html xmlns:fb="http://www.facebook.com/2008/fbml">
        <head>
            <title>UTC-Nuitinfo Login</title>
            <style>
            body {
            font-family: 'Lucida Grande', Verdana, Arial, sans-serif;
            }
            h1 a {
            text-decoration: none;
            color: #3b5998;
            }
            h1 a:hover {
            text-decoration: underline;
            }
            </style>
        </head>
        
        <body>
            <h1>UTC-Nuitinfo Login</h1>

            <div>
            Login using OAuth 2.0 handled by the PHP SDK:
            <a href="<?php echo $loginUrl; ?>">Login with Facebook</a>
            </div>
        </body>
    </html>
<?php endif ?>
