<?php

// required parameters: 
//  user_id=...
//  access_token=...
//  friend_id=...

// optional parameters:
//  max_items=...

require 'facebook-php-sdk/src/facebook.php';
require 'util.php';

function getGiftRecommandations($friendData, $max_items) {
    $recommandations = array();
    
    $requests = array('books', 'movies', 'music');
    for($i = 0; $i < sizeof($requests); ++$i) {
        $reqName = $requests[$i];
        if(!is_array($friendData[$reqName])) {
            continue;
        }
        if(sizeof($friendData[$reqName]) == 0) {
            continue;
        }
        usort($friendData[$reqName], compare_desc_by_created_time);
        
        $max_items_req = min($max_items, sizeof($friendData[$reqName]));
        for($j = 0; $j < $max_items_req; ++$j) {
            $element = $friendData[$reqName][$j];
            $newRecommandation = array(
                'category' => $reqName,
                'name' => $element['name'],
                'id' => $element['id'],
                'created_time' => $element['created_time'],
            );
            array_push($recommandations, $newRecommandation);
        }
    }
    return $recommandations;
}

$user_id = $_GET["user_id"];
$friend_id = $_GET["friend_id"];
$access_token = $_GET["access_token"];
$max_items = $_GET["max_items"];
if(is_null($max_items)) {
    $max_items = 10;
}

// temporary only
//$user_id = 1526860101;
//$access_token = 'AAAD59V4w2QsBAHvZAVa4kL55dfAOIEEsdqyAQWn1FZART28hhRKcKZCom8EQXTWmBtq4614bpFuSxyUyhUmUIGiLLKSGOUZD';
//$friend_id = 29709340;

// Create our Application instance
$facebook = new Facebook(array(
    'appId' => '274832242563339',
    'secret' => '4946a0747eb4c8b07340c856bd6a741a',
));

if ($user_id) {
    $requests = array('books', 'movies', 'music');
    for($i = 0; $i < sizeof($requests); ++$i) {
        $reqName = $requests[$i];
        $friendDataUrl = 'https://graph.facebook.com/' . $friend_id . '/' . $reqName . '?access_token=' . $access_token;
        $friendData[$reqName] = json_fetch_and_decode($friendDataUrl, true);
        $friendData[$reqName] = $friendData[$reqName]['data'];
    }
    $recommandations = getGiftRecommandations($friendData, $max_items);
    echo json_encode($recommandations);
} else {
    $error = array('error' => 'User not connected');
    echo json_encode($error);
    exit(-1);
}
