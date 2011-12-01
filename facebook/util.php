<?php

function json_fetch_and_decode($url, $assoc_array = true) {
    $json_str = @file_get_contents($url);
    return json_decode($json_str, $assoc_array);
}

?>
