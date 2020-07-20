job("devopstask6.1")
{
 description ("DevOpstask6")
scm {
github ('arushi09-hub/devopstask6','master')
}
configure { it / 'triggers' / 'com.cloudbees.jenkins.GitHubPushTrigger' / 'spec' }
steps{
shell('sudo rm -rf /website
sudo mkdir /website
sudo cp -rvf *.html /website


sudo rm -rf /kube
sudo mkdir /kube
sudo cp -rvf *.yml /kube')
}
triggers {
        upstream('devopstask6.2', 'SUCCESS')
    }
}
job("devopstask6.2")
{
description ("DevOpstask6")
steps{
shell('''if sudo kubectl get pv | grep pv
then
echo "Pv already Created"
sudo kubectl apply -f /kube/pv.yml
else
sudo kubectl create -f /kube/pv.yml
fi




if sudo kubectl get pvc | grep pvc
then
echo "Pvc already Created"
sudo kubectl apply -f /kube/pvc.yml
else
sudo kubectl create -f /kube/pvc.yml
fi


if  sudo kubectl get deploy | grep web
then 
echo "Deployment Already Created"
sudo kubectl apply -f /kube/deploy.yml
else
sudo kubectl create -f /kube/deploy.yml
fi

sudo sleep 20
status=$(sudo kubectl get pods -o 'jsonpath={.items[0].metadata.name}')
sudo kubectl get all
sudo kubectl cp /website/index.html $status:/usr/local/apache2/htdocs/''')
}
 triggers {
        upstream('devopstask6.3', 'SUCCESS')
    }
}
job("devopstask6.3")
{
description ("DevOpstaask6")
steps{
shell('''export status=$(curl -o /dev/null -sw "%{http_code}" http://192.168.99.101:31000/index.html)
if [ $status==200 ]
then
echo "Deployed"
exit 0
else
echo "status_code:$status"
exit 1
fi''')
}
triggers {
        upstream('devopstask6.4', 'SUCCESS')
    }
}
job("devopstask6.4")
{
description ("DevOpstask6")
 authenticationToken('mail')

 publishers {
        mailer('arushishukla09@gmail.com', true, true)
    }
triggers {
        upstream('devopstask6.3', 'SUCCESS')
    }
}
