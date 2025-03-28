# load-maps-demo

The getMapItemList method fetches a list of devices, then concurrently loads maps and their images for each device, building a final list of MapItemUi. 
Errors are safely handled using runCatching and supervisorScope to prevent one failure from cancelling the entire operation.


<img width="405" alt="image" src="https://github.com/user-attachments/assets/badf6aa1-6b14-4278-b644-d8ae4578724c" />
