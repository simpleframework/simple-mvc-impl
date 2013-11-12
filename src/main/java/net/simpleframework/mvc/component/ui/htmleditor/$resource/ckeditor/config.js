/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights
 *          reserved. For licensing, see LICENSE.html or
 *          http://ckeditor.com/license
 */
CKEDITOR.editorConfig = function(config) {  
  config.skin='moonocolor';
  
  config.font_names = '宋体;黑体;楷体;幼园;arial;comic sans ms;courier new;georgia;' + 
                      'lucida sans unicode;tahoma;times new roman;trebuchet ms;verdana';
  
  config.smiley_descriptions = [];
  config.smiley_columns = 15;
  config.smiley_images = [];
  for ( var i = 0; i <= 89; i++) {
    config.smiley_images.push(i + '.gif');
  }
  
  config.allowedContent = true;
  
  // 代码编辑
  config.extraPlugins = 'syntaxhighlight';
};
