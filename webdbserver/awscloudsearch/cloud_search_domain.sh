#! /bin/bash

export DOMAIN="cloudtrail-1"
export REGION="us-east-1"

aws cloudsearch create-domain --region $REGION --domain-name $DOMAIN

aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name event_name 				--type literal --search-enabled true --facet-enabled true --return-enabled true --sort-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name aws_region 				--type literal --search-enabled true --facet-enabled true --return-enabled true --sort-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name event_id 					--type literal --search-enabled true --facet-enabled false --return-enabled true --sort-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name request_id 				--type literal --search-enabled true --facet-enabled false --return-enabled true --sort-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name source_ip_address 			--type literal --search-enabled true --facet-enabled true --return-enabled true --sort-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name user_identity_account_id 	--type literal --search-enabled true --facet-enabled true --return-enabled true --sort-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name user_identity_type 		--type literal --search-enabled true --facet-enabled true --return-enabled true --sort-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name user_identity_user_name 	--type literal --search-enabled true --facet-enabled true --return-enabled true --sort-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name event_source 				--type literal --search-enabled true --facet-enabled true --return-enabled true --sort-enabled false

aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name error_code 				--type int --search-enabled true --facet-enabled true --return-enabled true --sort-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name error_message 				--type text --return-enabled true --sort-enabled true --highlight-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name user_agent 				--type text --return-enabled true --sort-enabled false --highlight-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name user_identity_arn 			--type text --return-enabled true --sort-enabled false --highlight-enabled false
aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name event_time 				--type date --search-enabled true --facet-enabled false --return-enabled true --sort-enabled true

aws cloudsearch define-index-field --region $REGION --domain-name $DOMAIN --name raw 						--type text --return-enabled true --sort-enabled false --highlight-enabled true

aws cloudsearch index-documents --region $REGION --domain-name $DOMAIN


