<?php

function json_fetch_and_decode($url, $assoc_array = true) {
    $json_str = @file_get_contents($url);
    return json_decode($json_str, $assoc_array);
}

function compare_desc_by_created_time($a, $b) {
    $field = 'created_time';
    return -1 * strcmp($a[$field], $b[$field]);
}

function compare_asc_by_name($a, $b) {
    $field = 'name';
    return strcmp($a[$field], $b[$field]);
}

?>
