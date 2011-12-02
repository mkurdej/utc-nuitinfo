<?php
/**
* Copyright 2011 Facebook, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may
* not use this file except in compliance with the License. You may obtain
* a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/

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

// Login or logout url will be needed depending on current user state.
if ($user) {
  $logoutUrl = $facebook->getLogoutUrl();
} else {
    $permissionScope = 'user_likes,friends_birthday,friends_likes'; //offline_access
    $loginParams = array(
        scope => $permissionScope,
        redirect_uri => 'http://www.floriandubois.com/nuitinfo/facebook/test-facebook-sdk.php'
    );
  $loginUrl = $facebook->getLoginUrl($loginParams);
}

// This call will always work since we are fetching public data.
$naitik = $facebook->api('/naitik');

?>
<!doctype html>
<html xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
<title>php-sdk</title>
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
<h1>php-sdk</h1>

<?php if ($user): ?>
<a href="<?php echo $logoutUrl; ?>">Logout</a>
<?php else: ?>
<div>
Login using OAuth 2.0 handled by the PHP SDK:
<a href="<?php echo $loginUrl; ?>">Login with Facebook</a>
</div>
<?php endif ?>

<h3>PHP Session</h3>
<pre><?php print_r($_SESSION); ?></pre>

<?php if ($user): ?>
<h3>You</h3>
<img src="https://graph.facebook.com/<?php echo $user; ?>/picture">

<h3>Your User Object (/me)</h3>
<pre><?php print_r($user_profile); ?></pre>
<?php else: ?>
<strong><em>You are not Connected.</em></strong>
<?php endif ?>

<h3>Public profile of Naitik</h3>
<img src="https://graph.facebook.com/naitik/picture">
<?php echo $naitik['name']; ?>

<br/>

<?php
    // Get the current access token
    $accessToken = $facebook->getAccessToken();
    // Get friends list URL
    // http://developers.facebook.com/docs/reference/api/
    $friendListUrl = 'https://graph.facebook.com/me/friends?access_token=' . $accessToken;

    $friendList = json_fetch_and_decode($friendListUrl, true);
    $friendList = $friendList['data'];
    
    $md_name = $friendList[1]['name'];
    $friendId = $friendList[1]['id'];
    
    $requests = array('books', 'likes', 'movies', 'music');
    for($i = 0; $i < sizeof($requests); ++$i) {
        $reqName = $requests[$i];
        $friendDataUrl = 'https://graph.facebook.com/' . $friendId . '/' . $reqName . '?access_token=' . $accessToken;
        $friendData[$reqName] = json_fetch_and_decode($friendDataUrl, true);
        $friendData[$reqName] = $friendData[$reqName]['data'];
    }
    
    //$recommandations = getGiftRecommandations($friendData);
    
    echo 'Friend Id = ' . $friendId . '\n';
    echo 'Books = ' . json_encode($friendData['books']) . '\n';
    echo 'Likes = ' . json_encode($friendData['likes']) . '\n';
    echo 'Movies = ' . json_encode($friendData['movies']) . '\n';
    echo 'Music = ' . json_encode($friendData['music']) . '\n';
?>

<a href="<?php echo $friendListUrl; ?>">Get friend list</a>
    
<br/>

</body>
</html>
